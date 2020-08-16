package org.study.account.config

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.springframework.beans.factory.BeanFactoryAware
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

class CustomScalarGeneratorHooks(override val wiringFactory: KotlinDirectiveWiringFactory) : SchemaGeneratorHooks {

    /**
     * Register additional GraphQL scalar types.
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        Unit::class -> graphqlUnitType
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
        .coercing(UnitCoercing)
        .build()

private object UnitCoercing : Coercing<Unit, Unit> {
    override fun parseValue(input: Any?): Unit = Unit

    override fun parseLiteral(input: Any?): Unit = Unit

    override fun serialize(dataFetcherResult: Any?): Unit = Unit
}