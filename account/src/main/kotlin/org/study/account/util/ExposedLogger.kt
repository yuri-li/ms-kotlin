package org.study.account.util

import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager

class ExposedLogger(val logger: org.slf4j.Logger) : SqlLogger {
    override fun log(context: StatementContext, transaction: Transaction) {
        logger.info("SQL: ${context.expandArgs(transaction)}")
    }
    companion object{
        fun addLogger(log: org.slf4j.Logger) = TransactionManager.current().addLogger(ExposedLogger(log))
    }
}