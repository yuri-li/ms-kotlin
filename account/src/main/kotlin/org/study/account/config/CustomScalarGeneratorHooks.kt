package org.study.account.config

import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.joda.time.DateTime
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Configuration
import org.study.common.util.toDateTime
import org.study.common.util.toDatetimeFormatter
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

@Configuration
class CustomScalarGeneratorHooks : SchemaGeneratorHooks {

    /**
     * Register additional GraphQL scalar types.
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        Unit::class -> graphqlUnitType
        DateTime::class -> graphqlDateTimeType
        else -> null
    }

    /**
     * Register Reactor Mono monad type.
     */
    override fun willResolveMonad(type: KType): KType = when (type.classifier) {
        Mono::class -> type.arguments.firstOrNull()?.type
        else -> type
    } ?: type

    /**
     * Exclude the Spring bean factory interface
     */
    override fun isValidSuperclass(kClass: KClass<*>): Boolean {
        return when {
            kClass.isSubclassOf(BeanFactoryAware::class) -> false
            else -> super.isValidSuperclass(kClass)
        }
    }
}

internal val graphqlUnitType = GraphQLScalarType.newScalar()
        .name("Unit")
        .description("Unit in Kotlin corresponds to the void in Java")
        .coercing(object : CustomCoercing<Unit, Unit>() {
            override fun write(s: Unit) = s

            override fun read(t: Unit) = t
        })
        .build()

internal val graphqlDateTimeType = GraphQLScalarType.newScalar()
        .name("DateTime")
        .description("org.joda.time.DateTime")
        .coercing(object : CustomCoercing<DateTime, String>() {
            override fun write(s: DateTime): String = s.toString(toDatetimeFormatter())

            override fun read(t: String): DateTime = t.toDateTime()
        })
        .build()

abstract class CustomCoercing<S, T> : Coercing<S, T> {
    final override fun serialize(dataFetcherResult: Any): T = write(dataFetcherResult as S)
    final override fun parseValue(input: Any): S {
        println("--------")
        return read(input as T)
    }
    final override fun parseLiteral(input: Any): S {
        println("=========")
        return read(input as T)
    }

    abstract fun write(s: S): T
    abstract fun read(t: T): S
}


