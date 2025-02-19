package net.ririfa.des

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class DES : JavaPlugin() {
	companion object {
		val thread: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

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