package net.ririfa.des.manager

import net.ririfa.des.DB
import net.ririfa.des.manager.DataManager.Tables
import net.ririfa.des.modules.economy.util.Player
import net.ririfa.des.util.DBT
import net.ririfa.des.util.ShortUUID
import net.ririfa.des.util.then
import org.jetbrains.exposed.sql.update
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

object CacheManager {
	private val playerCache: MutableMap<String, PlayerCache> = mutableMapOf()
	private val updateQueue: ConcurrentLinkedQueue<DBUpdateRequest> = ConcurrentLinkedQueue()

	private val executor = Executors.newSingleThreadExecutor()
	private val lock = ReentrantLock()
	private val notEmpty: Condition = lock.newCondition()

	init {
		executor.execute {
			while (true) {
				lock.lock()
				try {
					while (updateQueue.isEmpty()) {
						notEmpty.await()
					}
					processDBUpdates()
				} finally {
					lock.unlock()
				}
			}
		}
	}

	fun getPlayerData(uuid: ShortUUID): Player? {
		val su = uuid.toShortString()
		return playerCache[su]?.player ?: run {
			DB.getPlayerData(uuid)?.also {
				playerCache[su] = PlayerCache(System.currentTimeMillis(), it)
			}
		}
	}

	fun erasePlayerData(uuid: ShortUUID) {
		val su = uuid.toShortString()
		playerCache.remove(su)
	}

	private fun processDBUpdates() {
		while (true) {
			val request = updateQueue.poll() ?: break
			DBT {
				Tables.Players
					.update({
						Tables.Players.shortUUID eq request.uuid.toShortString()
					}) {
						it[balance] = request.newBalance
					}
			}
		}
	}

	fun cleanUpCache() {
		val now = System.currentTimeMillis()
		val expirationTime = 1000 * 60 * 10

		playerCache.entries.removeIf { (_, cache) ->
			now - cache.lastUsed > expirationTime
		}
	}

	data class PlayerCache(
		val lastUsed: Long,
		val player: Player
	)

	data class DBUpdateRequest(
		val uuid: ShortUUID,
		val newBalance: Double
	)

	// > ==== プレイヤー操作 ==== < \\
	fun withdrawPlayerBalance(player: Player, amount: Double): Boolean {
		val cache = getCache(player)

		if (cache.player.balance < amount) {
			return false
		}

		val newBalance = cache.player.balance - amount
		updatePlayerBalance(cache.player.uuid, newBalance)
		return true
	}

	fun depositPlayerBalance(player: Player, amount: Double): Boolean {
		val cache = getCache(player)

		val newBalance = cache.player.balance + amount
		updatePlayerBalance(cache.player.uuid, newBalance)
		return true
	}

	fun updatePlayerBalance(uuid: ShortUUID, newBalance: Double) {
		val su = uuid.toShortString()
		playerCache[su]?.let { cache ->
			playerCache[su] = cache.copy(player = cache.player.copy(balance = newBalance), lastUsed = System.currentTimeMillis())

			lock.lock()
			try {
				updateQueue.add(DBUpdateRequest(uuid, newBalance))
				notEmpty.signal()
			} finally {
				lock.unlock()
			}
		}
	}

	private fun getCache(player: Player): PlayerCache {
		val uuid = player.uuid
		return playerCache[uuid.toShortString()]
			?: DB.getPlayerData(uuid)?.let { PlayerCache(System.currentTimeMillis(), it) }
			?: PlayerCache(
				System.currentTimeMillis(),
				Player.createNew(uuid, player.name)
			).then {
				playerCache[uuid.toShortString()] = it
				DB.createNewPlayerToDB(it.player)
				return@then
			}
	}
}
