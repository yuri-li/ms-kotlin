package org.study.account.graphql

import com.expediagroup.graphql.spring.operations.Mutation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.study.account.service.TableService

@Component
class TableMutation(val service: TableService) : Mutation {
    val log = LoggerFactory.getLogger(this::class.java)

    fun doNothing(){
        log.info("just for test")
    }
    fun createTables() {
        log.info("create tables")

        service.createTables()
    }
    fun insertTestData() {
        log.info("insert test data")

        service.insertTestData()
    }
}