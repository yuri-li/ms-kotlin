package org.study.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.study.common.util.openUrl
import java.util.*

//internal fun getDirectiveName(kClass: KClass<out Annotation>): String = kClass.simpleName!!.toLowerCase()

@SpringBootApplication
class App {
    /*@Bean
    fun directiveWiringFactory(): KotlinDirectiveWiringFactory = object : KotlinDirectiveWiringFactory() {
        override fun getSchemaDirectiveWiring(
                environment: KotlinSchemaDirectiveEnvironment<GraphQLDirectiveContainer>
        ): KotlinSchemaDirectiveWiring? = when (environment.directive.name) {
            getDirectiveName(OAuth2::class) -> OAuth2SchemaDirectiveWiring()
            else -> null
        }
    }*/
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    runApplication<App>(*args)
    "http://localhost:8080/playground".openUrl()
}