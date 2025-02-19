package net.ririfa.des.util

import net.ririfa.des.DataManager
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("FunctionName")
fun DB(block: Transaction.() -> Unit) {
	transaction(DataManager.memoryDB!!, block)
}