package org.study.account

import graphql.execution.DataFetcherExceptionHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.study.account.config.GlobalExceptionHandler
import org.study.common.util.openUrl

@SpringBootApplication
class App{
    @Bean
    fun dataFetcherExceptionHandler(): DataFetcherExceptionHandler = GlobalExceptionHandler()
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
    "http://localhost:8080/playground".openUrl()
}