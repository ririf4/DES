package net.ririfa.des.modules.economy

import net.milkbowl.vault.economy.EconomyResponse
import net.ririfa.des.*
import net.ririfa.des.config.ConfigManager.Currency
import net.ririfa.des.modules.Module
import net.ririfa.des.util.ShortUUID
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("OVERRIDE_DEPRECATION")
class Economy : Module(
	name = "Economy",
	clazz = Economy::class.java
), net.milkbowl.vault.economy.Economy {
	private var isEnabled = AtomicBoolean(false)

	override fun register(server: Server) {
		registerEventListener(EconomyListener.get)

		isEnabled.set(true)
	}

	override fun isEnabled(): Boolean = DP.isEnabled
	override fun getName(): String? = this.name
	override fun hasBankSupport(): Boolean = true
	override fun fractionalDigits(): Int = 2
	override fun currencyNamePlural(): String? = Config.currency[Currency.PLURAL]
	override fun currencyNameSingular(): String? = Config.currency[Currency.SINGULAR]
	override fun format(amount: Double): String? = String.format("%.2f", amount)
	override fun hasAccount(playerName: String?): Boolean = true
	override fun hasAccount(player: OfflinePlayer?): Boolean = true
	override fun hasAccount(playerName: String?, worldName: String?): Boolean = true
	override fun hasAccount(player: OfflinePlayer?, worldName: String?): Boolean = true

	// 口座は自動で必ず作成されるためこれらは実装しない
	override fun createBank(name: String?, player: String?): EconomyResponse? = null
	override fun createBank(name: String?, player: OfflinePlayer?): EconomyResponse? = null
	override fun deleteBank(name: String?): EconomyResponse? = null
	override fun createPlayerAccount(playerName: String?): Boolean = false
	override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean = false
	override fun createPlayerAccount(player: OfflinePlayer?): Boolean = false
	override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?): Boolean = false

	// region getBalance
	override fun getBalance(playerName: String?): Double =
		getBalance(playerName?.let { DP.server.getOfflinePlayer(it) })

	override fun getBalance(playerName: String?, world: String?): Double =
		getBalance(playerName?.let { DP.server.getOfflinePlayer(it) }, world)

	override fun getBalance(player: OfflinePlayer?, world: String?): Double =
		getBalance(player)

	/**
	 * Retrieves the balance of the specified player.
	 *
	 * @param player The OfflinePlayer whose balance is to be retrieved. Can be null.
	 * @return The balance of the player as a Double. Returns 0.0 if the player is null or no data is found.
	 */
	override fun getBalance(player: OfflinePlayer?): Double {
		val ss = ShortUUID.fromUUID(player?.uniqueId.toString())
		val pd = DB.getPlayerData(ss)
		return pd?.balance ?: 0.0
	}
	// endregion

	// region has
	override fun has(playerName: String?, amount: Double): Boolean =
		has(playerName?.let { DP.server.getOfflinePlayer(it) }, null, amount)

	override fun has(player: OfflinePlayer?, amount: Double): Boolean =
		has(player?.let { DP.server.getOfflinePlayer(it.uniqueId) }, null, amount)

	override fun has(playerName: String?, worldName: String?, amount: Double): Boolean =
		has(playerName?.let { DP.server.getOfflinePlayer(it) }, null, amount)

	/**
	 * Checks if a player has a specified amount of money in their balance for a given world.
	 *
	 * @param player The player whose balance is being checked. Can be null.
	 * @param worldName The name of the world to check the balance in. Can be null.
	 * @param amount The amount of money to check against the player's balance.
	 * @return True if the player's balance is greater than or equal to the specified amount, false otherwise.
	 */
	override fun has(player: OfflinePlayer?, worldName: String?, amount: Double): Boolean {
		val pd = DB.getPlayerData(ShortUUID.fromUUID(player?.uniqueId.toString()))
		val pb = pd?.balance ?: 0.0
		return pb >= amount
	}
	// endregion

	// region withdraw
	override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse? =
		withdrawPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, null, amount)

	override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse? =
		withdrawPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, worldName, amount)

	override fun withdrawPlayer(player: OfflinePlayer?, amount: Double): EconomyResponse? =
		withdrawPlayer(player, null, amount)

	/**
	 * Withdraws a specific amount of money from a player's balance in a specified world.
	 *
	 * @param player The player from whom the money will be withdrawn. Can be null.
	 * @param worldName The name of the world associated with the transaction. Can be null.
	 * @param amount The amount of money to withdraw. Must be non-negative.
	 * @return An instance of EconomyResponse indicating the result of the operation, including success or failure details.
	 */
	override fun withdrawPlayer(
		player: OfflinePlayer?,
		worldName: String?,
		amount: Double
	): EconomyResponse? {
		if (player == null || amount < 0) {
			return EconomyResponse(
				0.0,
				0.0,
				EconomyResponse.ResponseType.FAILURE,
				"無効なプレイヤーまたはマイナスの金額"
			)
		}

		val uuid = ShortUUID(player.uniqueId)
		val playerData = DC.getPlayerData(uuid)

		if (playerData == null) {
			return EconomyResponse(
				0.0,
				0.0,
				EconomyResponse.ResponseType.FAILURE,
				"プレイヤーが見つかりません"
			)
		}

		val success = DC.withdrawPlayerBalance(playerData, amount)

		return if (success) {
			val newBalance = playerData.balance - amount
			EconomyResponse(
				amount,
				newBalance,
				EconomyResponse.ResponseType.SUCCESS,
				null
			)
		} else {
			EconomyResponse(
				0.0,
				playerData.balance,
				EconomyResponse.ResponseType.FAILURE,
				"資金不足"
			)
		}

	}
	// endregion

	// region deposit
	override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse? =
		depositPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, null, amount)

	override fun depositPlayer(player: OfflinePlayer?, amount: Double): EconomyResponse? =
		depositPlayer(player, null, amount)

	override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse? =
		depositPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, worldName, amount)

	/**
	 * Deposits a specified amount of money to the balance of a player in a given world.
	 *
	 * @param player The OfflinePlayer to whom the money will be deposited. Can be null.
	 * @param worldName The name of the world associated with the transaction. Can be null.
	 * @param amount The amount of money to deposit. Must be non-negative.
	 * @return An instance of EconomyResponse indicating the result of the operation, including success or failure details.
	 */
	override fun depositPlayer(
		player: OfflinePlayer?,
		worldName: String?,
		amount: Double
	): EconomyResponse? {
		if (player == null || amount < 0) {
			return EconomyResponse(
				0.0,
				0.0,
				EconomyResponse.ResponseType.FAILURE,
				"無効なプレイヤーまたはマイナスの金額"
			)
		}

		val uuid = ShortUUID(player.uniqueId)
		val playerData = DC.getPlayerData(uuid)

		if (playerData == null) {
			return EconomyResponse(
				0.0,
				0.0,
				EconomyResponse.ResponseType.FAILURE,
				"プレイヤーが見つかりません"
			)
		}

		val success = DC.depositPlayerBalance(playerData, amount)

		return if (success) {
			val newBalance = playerData.balance + amount
			EconomyResponse(
				amount,
				newBalance,
				EconomyResponse.ResponseType.SUCCESS,
				null
			)
		} else {
			EconomyResponse(
				0.0,
				playerData.balance,
				EconomyResponse.ResponseType.FAILURE,
				"預金に失敗"
			)
		}
	}
	// endregion

	// region isBankOwner
	override fun isBankOwner(name: String?, playerName: String?): EconomyResponse? =
		isBankOwner(name, playerName?.let { DP.server.getOfflinePlayer(it) })

	override fun isBankOwner(name: String?, player: OfflinePlayer?): EconomyResponse? =
		DES.le.isBankOwner(name, player)
	// endregion

	// region isBankMember
	override fun isBankMember(name: String?, playerName: String?): EconomyResponse? =
		isBankMember(name, playerName?.let { DP.server.getOfflinePlayer(it) })

	override fun isBankMember(name: String?, player: OfflinePlayer?): EconomyResponse? =
		DES.le.isBankMember(name, player)
	// endregion

	override fun bankBalance(name: String?): EconomyResponse? =
		DES.le.bankBalance(name)

	override fun bankDeposit(name: String?, amount: Double): EconomyResponse? =
		DES.le.bankDeposit(name, amount)

	override fun bankWithdraw(name: String?, amount: Double): EconomyResponse? =
		DES.le.bankWithdraw(name, amount)


	// TODOゾーン(実装しないかも？)
	override fun bankHas(name: String?, amount: Double): EconomyResponse? {
		return null
	}

	override fun getBanks(): List<String?>? {
		return null
	}
}