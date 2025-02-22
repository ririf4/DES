package net.ririfa.des.util

import net.ririfa.des.manager.DataManager
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

@Suppress("FunctionName")
fun DB(block: Transaction.() -> Unit) {
	transaction(DataManager.dataBase, block)
}

@Suppress("FunctionName")
fun <T> DB(block: Transaction.() -> T): T {
	return transaction(DataManager.dataBase, block)
}