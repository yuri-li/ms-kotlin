package org.study.account.config

import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcherFactory
import org.springframework.context.ApplicationContext
import kotlin.reflect.KFunction
import com.expediagroup.graphql.spring.execution.SpringDataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@Configuration
class CustomDataFetcherFactoryProvider(
        private val springDataFetcherFactory: SpringDataFetcherFactory,
        private val objectMapper: ObjectMapper,
        private val applicationContext: ApplicationContext
) : SimpleKotlinDataFetcherFactoryProvider(objectMapper) {

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) = DataFetcherFactory {
        CustomFunctionDataFetcher(
                target = target,
                fn = kFunction,
                objectMapper = objectMapper,
                appContext = applicationContext
        )
    }

    override fun propertyDataFetcherFactory(kClass: KClass<*>, kProperty: KProperty<*>): DataFetcherFactory<Any?> =
            if (kProperty.isLateinit) {
                springDataFetcherFactory
            } else {
                super.propertyDataFetcherFactory(kClass, kProperty)
            }
}

/**
 * Custom function data fetcher that adds support for Reactor Mono.
 */
class CustomFunctionDataFetcher(
        target: Any?,
        fn: KFunction<*>,
        objectMapper: ObjectMapper,
        appContext: ApplicationContext
) : SpringDataFetcher(target, fn, objectMapper, appContext) {

    override fun get(environment: DataFetchingEnvironment): Any? = when (val result = super.get(environment)) {
        is Mono<*> -> result.toFuture()
        else -> result
    }
}
