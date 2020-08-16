package org.study.account.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import graphql.Assert
import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.execution.ExecutionPath
import graphql.language.SourceLocation

class ErrorCodeException(val code: String, override val message: String) : RuntimeException(message)

@JsonIgnoreProperties("exception")
class ErrorCodeDataFetchingGraphQLError(val path: ExecutionPath, val exception: ErrorCodeException, val sourceLocation: SourceLocation) : GraphQLError {
    override fun getMessage(): String = exception.message
    override fun getErrorType(): ErrorClassification = ErrorType.DataFetchingException
    override fun getLocations(): List<SourceLocation> = listOf(sourceLocation)
    override fun getPath(): List<Any> = Assert.assertNotNull(path).toList()
    override fun getExtensions(): Map<String, Any> = mapOf("code" to exception.code, "reason" to "ErrorCodeException")
}
