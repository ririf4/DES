package net.ririfa.des.modules

import org.jetbrains.annotations.NotNull

abstract class Module(
	@NotNull
	val name: String,
	val description: String = "A ${name.lowercase()} module",
	@NotNull
	val clazz: Class<*>
) {
	abstract fun register()
}