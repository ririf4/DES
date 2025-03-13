package net.ririfa.des.translation

import net.kyori.adventure.text.Component
import net.ririfa.langman.def.MessageProviderDefault
import org.bukkit.entity.Player

class DESMSGProvider(private val player: Player) : MessageProviderDefault<DESMSGProvider, Component>(
	Component::class.java
) {
	override fun getLanguage(): String {
		return player.locale().language
	}
}

fun Player.adapt(): DESMSGProvider = DESMSGProvider(this)