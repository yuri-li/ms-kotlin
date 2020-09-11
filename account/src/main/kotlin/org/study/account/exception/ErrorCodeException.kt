package org.study.account.exception

import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import graphql.Assert
import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.execution.ExecutionPath
import graphql.language.SourceLocation

class ErrorCodeException(val code: String, override val message: String) : RuntimeException(message)

@JsonIgnoreProperties("exception")
class ErrorCodeDataFetchingGraphQLError(@JsonIgnore val ex:ErrorCodeException) : GraphQLError {
    override fun getMessage(): String = ex.message
    @JsonIgnore
    override fun getErrorType(): ErrorClassification = ErrorType.DataFetchingException
    override fun getLocations(): List<SourceLocation> = emptyList()
    override fun getPath(): List<Any> = emptyList()
    override fun getExtensions(): Map<String, Any> = mapOf("code" to ex.code, "reason" to "ErrorCodeException")
}
