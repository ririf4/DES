package net.ririfa.des.modules.economy

import net.milkbowl.vault.economy.EconomyResponse
import net.ririfa.des.Config
import net.ririfa.des.DB
import net.ririfa.des.DP
import net.ririfa.des.config.ConfigManager.Currency
import net.ririfa.des.modules.Module
import net.ririfa.des.util.ShortUUID
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import java.util.concurrent.atomic.AtomicBoolean

class Economy : Module(
	name = "Economy",
	clazz = Economy::class.java
), net.milkbowl.vault.economy.Economy {
	private var isEnabled = AtomicBoolean(false)

	override fun register(server: Server) {
		registerEventListener(EconomyListener.get)

		isEnabled.set(true)
	}

	override fun isEnabled(): Boolean =
		DP.isEnabled

	override fun getName(): String? =
		this.name

	override fun hasBankSupport(): Boolean =
		true

	override fun fractionalDigits(): Int = 2
	override fun currencyNamePlural(): String? = Config.currency[Currency.PLURAL]
	override fun currencyNameSingular(): String? = Config.currency[Currency.SINGULAR]
	override fun format(amount: Double): String? = String.format("%.2f", amount)

	// region hasAccount
	override fun hasAccount(playerName: String?): Boolean {
		return true
	}

	override fun hasAccount(player: OfflinePlayer?): Boolean {
		return true
	}

	override fun hasAccount(playerName: String?, worldName: String?): Boolean {
		return true
	}

	override fun hasAccount(player: OfflinePlayer?, worldName: String?): Boolean {
		return true
	}
	// endregion

	// region getBalance
	override fun getBalance(playerName: String?): Double =
		getBalance(playerName?.let { DP.server.getOfflinePlayer(it) })

	override fun getBalance(playerName: String?, world: String?): Double =
		getBalance(playerName?.let { DP.server.getOfflinePlayer(it) }, world)

	override fun getBalance(player: OfflinePlayer?, world: String?): Double =
		getBalance(player)

	/**
	 * プレイヤーの残高を取得する
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
	 * プレイヤーの残高が指定した量以上かどうかを取得する
	 */
	override fun has(player: OfflinePlayer?, worldName: String?, amount: Double): Boolean {
		val pd = DB.getPlayerData(ShortUUID.fromUUID(player?.uniqueId.toString()))
		val pb = pd?.balance ?: 0.0
		return pb >= amount
	}
	// endregion

	override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse? =
		withdrawPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, null, amount)

	override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse? =
		withdrawPlayer(playerName?.let { DP.server.getOfflinePlayer(it) }, worldName, amount)

	override fun withdrawPlayer(player: OfflinePlayer?, amount: Double): EconomyResponse? =
		withdrawPlayer(player, null, amount)

	override fun withdrawPlayer(
		player: OfflinePlayer?,
		worldName: String?,
		amount: Double
	): EconomyResponse? {
		// TODO: 残高交換
	}

	override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse? {

	}

	override fun depositPlayer(player: OfflinePlayer?, amount: Double): EconomyResponse? {

	}

	override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse? {

	}

	override fun depositPlayer(
		player: OfflinePlayer?,
		worldName: String?,
		amount: Double
	): EconomyResponse? {

	}

	// 口座は自動で必ず作成されるためこれらは実装しない
	override fun createBank(name: String?, player: String?): EconomyResponse? = null
	override fun createBank(name: String?, player: OfflinePlayer?): EconomyResponse? = null
	override fun deleteBank(name: String?): EconomyResponse? = null


	// TODOゾーン
	override fun bankBalance(name: String?): EconomyResponse? {

	}

	override fun bankHas(name: String?, amount: Double): EconomyResponse? {

	}

	override fun bankWithdraw(name: String?, amount: Double): EconomyResponse? {

	}

	override fun bankDeposit(name: String?, amount: Double): EconomyResponse? {

	}

	override fun isBankOwner(name: String?, playerName: String?): EconomyResponse? {

	}

	override fun isBankOwner(name: String?, player: OfflinePlayer?): EconomyResponse? {

	}

	override fun isBankMember(name: String?, playerName: String?): EconomyResponse? {

	}

	override fun isBankMember(name: String?, player: OfflinePlayer?): EconomyResponse? {

	}

	override fun getBanks(): List<String?>? {

	}

	override fun createPlayerAccount(playerName: String?): Boolean {

	}

	override fun createPlayerAccount(player: OfflinePlayer?): Boolean {

	}

	override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {

	}

	override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?): Boolean {

	}
}