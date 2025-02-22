package net.ririfa.des.manager

import net.ririfa.des.util.DB
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object DataManager {
	val URL: String? = System.getProperty("des.db.url")
	val USER: String? = System.getProperty("des.db.user")
	val PW: String? = System.getProperty("des.db.password")

	lateinit var dataBase: Database
		private set

	fun setUpDatabase() {
		if (URL == null || USER == null || PW == null) {
			throw IllegalStateException("データベースURL/USER/PWがセットされていません、プロパティ'des.db.url'を設定してください")
		}
		dataBase = Database.Companion.connect(
			"jdbc:postgresql://$URL",
			driver = "org.postgresql.Driver",
			user = USER,
			password = PW
		)

		transaction(dataBase) {
			SchemaUtils.create(Tables.Players)
		}
	}

	object Tables {
		object Players : Table("players") {
			val uuid = uuid("uuid")
			val name = text("name")
			val balance = double("balance")


			override val primaryKey = PrimaryKey(uuid)
		}
	}
}