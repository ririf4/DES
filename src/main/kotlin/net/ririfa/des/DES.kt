package net.ririfa.des

import com.google.gson.Gson
import net.ririfa.des.manager.DataManager
import net.ririfa.des.modules.Modules
import net.ririfa.des.modules.economy.Economy
import net.ririfa.des.modules.inventorySync.InventorySync
import net.ririfa.des.util.CannotFindEconomyException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class DES : JavaPlugin() {
	companion object {
		const val LANDS = "lands"

		val logger: Logger = LoggerFactory.getLogger(DES::class.simpleName)

		val get: DES
			get() = getPlugin(DES::class.java)

		lateinit var le: net.milkbowl.vault.economy.Economy
	}

	val dataFile = dataFolder.resolve("data.db")

	override fun onLoad() {
		Logger.info("DESを読み込み中です")

		Logger.info("データベースに接続します")
		DataManager.setUpDatabase()
	}

	override fun onEnable() {
		Modules.registerModules(server) {
			listOf(
				Economy(),
				InventorySync()
			)
		}

		le = getEconomyByPluginName(LANDS) ?: throw CannotFindEconomyException("Economy plugin '$LANDS' not found")
	}

	override fun onDisable() {

	}

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		return false
	}

	@Suppress("SameParameterValue")
	private fun getEconomyByPluginName(pluginName: String?): net.milkbowl.vault.economy.Economy? {
		val servicesManager = Bukkit.getServer().servicesManager
		val providers: Collection<RegisteredServiceProvider<net.milkbowl.vault.economy.Economy?>?> =
			servicesManager.getRegistrations<net.milkbowl.vault.economy.Economy?>(net.milkbowl.vault.economy.Economy::class.java)

		for (provider in providers) {
			val providerPluginName = provider?.plugin?.name

			if (providerPluginName.equals(pluginName, ignoreCase = true)) {
				if (provider != null) {
					return provider.getProvider()
				}
			}
		}
		return null
	}

	private val listener = object : Listener {
		@EventHandler
		fun onPlayerJoin(event: PlayerJoinEvent) {
		}

		@EventHandler
		fun onPlayerQuit(event: PlayerQuitEvent) {
			val gson = Gson()
			val player = event.player
			val playerInventory = player.inventory
		}
	}
}