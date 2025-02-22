package net.ririfa.des

import net.ririfa.des.manager.DataManager
import net.ririfa.des.modules.Modules
import net.ririfa.des.modules.economy.Economy
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DES : JavaPlugin() {
	companion object {
		val logger: Logger = LoggerFactory.getLogger(DES::class.simpleName)

		val get: DES
			get() = getPlugin(DES::class.java)
	}

	val dataFile = dataFolder.resolve("data.db")

	override fun onLoad() {
		Logger.info("DESを読み込み中です")

		Logger.info("データベースに接続します")
		DataManager.setUpDatabase()
	}

	override fun onEnable() {
		Modules.registerModules(
			Economy()
		)
	}

	override fun onDisable() {

	}

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		return false
	}


}