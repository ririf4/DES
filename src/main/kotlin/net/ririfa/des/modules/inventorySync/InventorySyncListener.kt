package net.ririfa.des.modules.inventorySync

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class InventorySyncListener : Listener {
	private val inventoryData = mutableMapOf<String, ByteArray>()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val item = player.inventory.contents

		val serializedData = serializeInventory(item)
		inventoryData[player.uniqueId.toString()] = serializedData
	}

	private fun serializeInventory(items: Array<ItemStack?>): ByteArray {
		val byteArrayOutputStream = ByteArrayOutputStream()
		val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

		objectOutputStream.writeObject(items)
		objectOutputStream.close()

		return byteArrayOutputStream.toByteArray()
	}

	data class PlayerInventory(
		val data: ByteArray
	) {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as PlayerInventory

			return data.contentEquals(other.data)
		}

		override fun hashCode(): Int {
			return data.contentHashCode()
		}
	}
}