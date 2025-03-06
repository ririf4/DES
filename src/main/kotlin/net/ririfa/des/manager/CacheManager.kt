package net.ririfa.des.manager

import net.ririfa.des.DB
import net.ririfa.des.modules.economy.util.Player
import net.ririfa.des.util.ShortUUID

object CacheManager {
	// ShortUUID -> Player
	val playerCache = mutableMapOf<String, Player>()

	fun getPlayerData(uuid: ShortUUID): Player? {
		val su = uuid.toShortString()
		return playerCache[su] ?: run {
			DB.getPlayerData(uuid)?.also {
				playerCache[su] = it
			}
		}
	}

	fun erasePlayerData(uuid: ShortUUID) {
		val su = uuid.toShortString()
		playerCache.remove(su)
	}

	fun clearnUpCache() {

	}
}