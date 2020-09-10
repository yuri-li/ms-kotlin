package org.study.account.model.directive

import com.expediagroup.graphql.annotations.GraphQLDirective
import com.expediagroup.graphql.directives.KotlinFieldDirectiveEnvironment
import com.expediagroup.graphql.directives.KotlinSchemaDirectiveEnvironment
import com.expediagroup.graphql.directives.KotlinSchemaDirectiveWiring
import graphql.schema.*
import java.util.function.BiFunction

@GraphQLDirective(name = "lowercase", description = "Modifies the string field to lowercase")
annotation class Lowercase

class LowercaseSchemaDirectiveWiring : KotlinSchemaDirectiveWiring {

    /*override fun onInputObjectField(environment: KotlinSchemaDirectiveEnvironment<GraphQLInputObjectField>): GraphQLInputObjectField {
        val obj = environment.element
        return obj
    }*/


    override fun onField(environment: KotlinFieldDirectiveEnvironment): GraphQLFieldDefinition {
        val field = environment.element
        val originalDataFetcher: DataFetcher<*> = environment.getDataFetcher()


        val lowerCaseFetcher = DataFetcher { env ->
            val value = originalDataFetcher.get(env)
            value
        }
        environment.setDataFetcher(lowerCaseFetcher)
        return field
    }
}