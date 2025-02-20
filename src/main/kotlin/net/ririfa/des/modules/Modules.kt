package net.ririfa.des.modules

object Modules {
	private val modules: MutableList<Module> = mutableListOf()

	fun getModules(): List<Module> {
		return modules.toList()
	}

	fun registerModules(vararg modules: Module) {
		this.modules.addAll(modules)
	}
}