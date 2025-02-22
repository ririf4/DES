package net.ririfa.des.modules.economy.util

import java.util.UUID

data class Player(
	val uuid: UUID,
	val name: String,
	val balance: Double
) {
	// 明確なcreate関数を作成し、呼び出し側での処理を濁らせず、はっきりさせる
	fun create(
		uuid: UUID,
		name: String
	): Player {
		return Player(uuid, name, 0.0)
	}
}