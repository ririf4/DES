package net.ririfa.des.modules

object Modules {
	private val modules: MutableList<Module> = mutableListOf()

	fun getModules(): List<Module> {
		return modules.toList()
	}

	fun registerModules(vararg modules: Module) {
		this.modules.addAll(modules)
		modules.forEach { it.register() }
	}

	fun <T : Module> getModule(clazz: Class<T>): T {
		return modules.first { it.clazz == clazz } as T
	}
}