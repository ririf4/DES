package net.ririfa.des

import org.jetbrains.exposed.sql.Database

object DataManager {
	var memoryDB: Database? = null
	var fileDB: Database? = null

	val dataFile: String by lazy { DES.get.dataFile.absolutePath }

	fun setUpDatabase() {
		memoryDB = Database.connect(
			"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;MV_STORE=TRUE;AUTO_SERVER=TRUE",
			driver = "org.h2.Driver"
		)

		fileDB = Database.connect(
			"jdbc:h2:file:${dataFile};MODE=PostgreSQL;MV_STORE=TRUE;AUTO_SERVER=TRUE",
			driver = "org.h2.Driver"
		)
	}
}