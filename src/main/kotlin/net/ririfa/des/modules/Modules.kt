package net.ririfa.des.modules

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object Modules {
	val modules: MutableList<Module> = mutableListOf()

	fun getModules(): List<Module> {
		return modules.toList()
	}

	fun registerModules(server: Server, modules: () -> List<Module>) {
		this.modules.addAll(modules())
		modules().forEach { it.register(server) }
	}

	fun registerListeners(server: Server, plugin: JavaPlugin) {
		modules.forEach { module ->
			module.listeners.forEach { listener ->
				server.pluginManager.registerEvents(listener, plugin)
			}
		}
	}

	inline fun <reified T : Module> getModule(clazz: Class<T>): T {
		return modules.first { it.clazz == clazz } as T
	}
}