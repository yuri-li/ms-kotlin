package org.study.account

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import graphql.execution.DataFetcherExceptionHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.study.account.config.CustomScalarGeneratorHooks
import org.study.account.config.GlobalExceptionHandler
import org.study.common.util.openUrl
import java.util.*

@SpringBootApplication
class App{
    @Bean
    fun dataFetcherExceptionHandler(): DataFetcherExceptionHandler = GlobalExceptionHandler()

    @Bean
    fun hooks() = CustomScalarGeneratorHooks(KotlinDirectiveWiringFactory())
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    runApplication<App>(*args)
    "http://localhost:8080/playground".openUrl()
}