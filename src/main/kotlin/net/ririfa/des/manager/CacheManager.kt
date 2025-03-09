package net.ririfa.des.manager

import net.ririfa.des.DB
import net.ririfa.des.modules.economy.util.Player
import net.ririfa.des.util.ShortUUID

object CacheManager {
	// ShortUUID -> Player
	val playerCache: MutableMap<String, PlayerCache> = mutableMapOf()

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
}