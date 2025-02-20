package net.ririfa.des

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
	}

	override fun onEnable() {

	}

	override fun onDisable() {

	}

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		return false
	}
}