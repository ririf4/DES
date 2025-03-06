package net.ririfa.des.modules.economy.util

@ConsistentCopyVisibility
data class Player private constructor(
	val uuid: String,
	val name: String,
	val balance: Double
) {
	companion object {
		// 明確なcreateNew関数を作成し、呼び出し側での処理を濁らせず、はっきりさせる
		fun createNew(
			uuid: String,
			name: String
		): Player {
			return Player(uuid, name, 0.0)
		}

		// こちらも同様。
		fun build(
			uuid: String,
			name: String,
			balance: Double
		): Player {
			return Player(uuid, name, balance)
		}
	}
}