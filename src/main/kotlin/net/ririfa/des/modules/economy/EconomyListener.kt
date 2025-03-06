package net.ririfa.des.modules.economy

import net.ririfa.des.manager.CacheManager
import net.ririfa.des.util.ShortUUID
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class EconomyListener : Listener {
	companion object {
		val instance: EconomyListener by lazy { EconomyListener() }

		val get
			get() = instance
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val uuid = ShortUUID.fromUUID(player.uniqueId)
		CacheManager.getPlayerData(uuid)
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val uuid = player.uniqueId
		CacheManager.erasePlayerData(uuid)
	}
}