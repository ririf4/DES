package net.ririfa.des.config

import org.yaml.snakeyaml.Yaml

object ConfigManager {
	val yaml = Yaml()
	lateinit var config: Config

	data class Config(
		val currency: Map<Currency, String>,
	) {

	}

	enum class Currency {
		PLURAL,
		SINGULAR
	}
}