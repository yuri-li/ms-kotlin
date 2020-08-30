/*
package org.study

import graphql.schema.Coercing
import java.lang.reflect.ParameterizedType
import kotlin.reflect.typeOf

fun main() {
    val concering = PageCoercing()
    println(concering.serialize(Page<Int>(emptyList())))
    println(concering.serialize(Page(listOf(1,2))))
}

data class Page<T>(
        val value: List<T>
)

class PageCoercing : Coercing<Page<Any>, String> {
    override fun serialize(dataFetcherResult: Any): String {
        println("------")
        return handlePage((dataFetcherResult as Page<Any>).value)
    }

    private fun <T : Any> handlePage(data: T): String = when(data){
        is List<*> -> if(data.isEmpty()){
            "EmptyPage"
        }else{
            handlePage(data.get(0) as T)
        }
            else -> "${data::class.simpleName!!}Page"
    }

    override fun parseValue(input: Any?): Page<Any> {
        TODO("Not yet implemented")
    }

    override fun parseLiteral(input: Any?): Page<Any> {
        TODO("Not yet implemented")
    }
}*/
