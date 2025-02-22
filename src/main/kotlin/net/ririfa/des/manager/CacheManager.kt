package net.ririfa.des.manager

import net.ririfa.des.modules.economy.util.Player
import net.ririfa.des.util.DB
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object CacheManager {
	val playerCache = mutableMapOf<UUID, Player>()

	fun getPlayerData(uuid: UUID): Player? {
		return playerCache[uuid] ?: DB<Player?> {
			DataManager.Tables.Players
				.selectAll()
				.where { DataManager.Tables.Players.uuid eq uuid }
				.singleOrNull()
				?.let {
					val player = Player(
						uuid = it[DataManager.Tables.Players.uuid],
						name = it[DataManager.Tables.Players.name],
						balance = it[DataManager.Tables.Players.balance]
					)
					playerCache[uuid] = player
					player
				}
		}
	}

	fun erasePlayerData(uuid: UUID) {
		playerCache.remove(uuid)
	}
}