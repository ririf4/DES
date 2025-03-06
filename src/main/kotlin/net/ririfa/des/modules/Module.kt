package net.ririfa.des.modules

import org.bukkit.Server
import org.bukkit.event.Listener
import org.jetbrains.annotations.NotNull
import kotlin.reflect.KClass

abstract class Module(
	@NotNull
	val name: String,
	val description: String = "A ${name.lowercase()} module",
	@NotNull
	val clazz: KClass<*>
) {
	val listeners: MutableList<Listener> = mutableListOf()

	abstract fun register(server: Server)

	fun registerEventListener(listener: Listener) {
		listeners.add(listener)
	}
}