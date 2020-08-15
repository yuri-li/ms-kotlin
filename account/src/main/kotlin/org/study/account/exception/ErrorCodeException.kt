package org.study.account.exception

import graphql.GraphqlErrorException
import graphql.language.SourceLocation

class ErrorCodeException(val code: String, override val message: String) : GraphqlErrorException(Builder()) {
    override fun getLocations(): List<SourceLocation>? = null

    override fun getExtensions(): Map<String, String> = mapOf("code" to code, "reason" to "ErrorCodeException")
}