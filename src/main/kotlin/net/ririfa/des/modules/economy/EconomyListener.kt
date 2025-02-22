package net.ririfa.des.modules.economy

import net.ririfa.des.manager.CacheManager
import net.ririfa.des.util.IEventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@IEventListener
class EconomyListener : Listener {
	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val uuid = player.uniqueId
		CacheManager.getPlayerData(uuid)
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val uuid = player.uniqueId
		CacheManager.erasePlayerData(uuid)
	}
}