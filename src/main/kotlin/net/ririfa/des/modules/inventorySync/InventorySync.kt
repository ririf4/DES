package net.ririfa.des.modules.inventorySync

import net.ririfa.des.modules.Module
import org.bukkit.Server

class InventorySync : Module(
	"InventorySync",
	"A module to sync inventory",
	InventorySync::class.java
) {
	override fun register(server: Server) {

	}
}