package net.ririfa.des.modules

abstract class Module(
	val name: String,
	val description: String = "No description provided."
) {
	abstract fun register()
}