package net.ririfa.des.util

import net.ririfa.des.manager.DataManager
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("FunctionName")
fun DBT(block: Transaction.() -> Unit) {
	transaction(DataManager.dataBase, block)
}

@Suppress("FunctionName")
fun <T> DBT(block: Transaction.() -> T): T {
	return transaction(DataManager.dataBase, block)
}