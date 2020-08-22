package org.study.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.study.common.util.openUrl
import java.util.*

@SpringBootApplication
class App

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    runApplication<App>(*args)
    "http://localhost:8080/playground".openUrl()
}