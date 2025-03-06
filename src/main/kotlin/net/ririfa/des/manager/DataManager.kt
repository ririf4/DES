package net.ririfa.des.manager

import net.ririfa.des.modules.economy.util.Player
import net.ririfa.des.util.DBT
import net.ririfa.des.util.ShortUUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

object DataManager {
	val URL: String? = System.getProperty("des.db.url")
	val USER: String? = System.getProperty("des.db.user")
	val PW: String? = System.getProperty("des.db.password")

	lateinit var dataBase: Database
		private set

	fun setUpDatabase() {
		if (URL == null || USER == null || PW == null) {
			throw IllegalStateException("データベースURL/USER/PWがセットされていません、\n プロパティ'des.db.url'/'des.db.user'/'des.db.password'を設定してください")
		}
		dataBase = Database.Companion.connect(
			"jdbc:postgresql://$URL",
			driver = "org.postgresql.Driver",
			user = USER,
			password = PW
		)

		DBT {
			SchemaUtils.create(Tables.Players)
		}
	}

	fun createNewPlayerToDB(player: Player) {
		DBT {
			Tables.Players.insert {
				it[shortUUID] = player.uuid.toShortString()
				it[name] = player.name
				it[balance] = player.balance
			}
		}
	}

	fun getPlayerData(shortUUID: ShortUUID): Player? {
		return DBT<Player?> {
			Tables.Players
				.selectAll()
				.where { Tables.Players.shortUUID eq shortUUID.toShortString() }
				.singleOrNull()
				?.let {
					Player.build(
						uuid = ShortUUID.fromShortString(it[Tables.Players.shortUUID]),
						name = it[Tables.Players.name],
						balance = it[Tables.Players.balance]
					)
				}
		}
	}

	object Tables {
		object Players : Table("players") {
			// ShortStringを格納する（UUIDよりも短いのでデータベースの容量を節約できる(わずかに)）
			/** [net.ririfa.des.util.ShortUUID] **/
			// あんまり"_"を使いたくないよぉ～
			val shortUUID = text("short_uuid")
			val name = text("name")
			val balance = double("balance")

			override val primaryKey = PrimaryKey(shortUUID)
		}
	}
}