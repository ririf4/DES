package net.ririfa.des.modules.economy.util

import net.ririfa.des.util.ShortUUID

@ConsistentCopyVisibility
data class Player private constructor(
	val uuid: ShortUUID,
	val name: String,
	val balance: Double
) {
	companion object {
		// 明確なcreateNew関数を作成し、呼び出し側での処理を濁らせず、はっきりさせる
		fun createNew(
			uuid: ShortUUID,
			name: String
		): Player {
			return Player(uuid, name, 0.0)
		}

		// こちらも同様。
		fun build(
			uuid: ShortUUID,
			name: String,
			balance: Double
		): Player {
			return Player(uuid, name, balance)
		}
	}
}