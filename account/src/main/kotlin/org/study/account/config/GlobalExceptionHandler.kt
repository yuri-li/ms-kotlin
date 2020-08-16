package org.study.account.config

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.slf4j.LoggerFactory
import org.study.account.exception.ErrorCodeDataFetchingGraphQLError
import org.study.account.exception.ErrorCodeException


class GlobalExceptionHandler : DataFetcherExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val exception = handlerParameters.exception

        log.error(exception.message)

        if (exception is ErrorCodeException) {
            return DataFetcherExceptionHandlerResult.newResult().error(ErrorCodeDataFetchingGraphQLError(handlerParameters.path, exception, handlerParameters.sourceLocation)).build()
        }
        if (exception is GraphQLError) {
            return DataFetcherExceptionHandlerResult.newResult().error(exception).build()
        }

        return DataFetcherExceptionHandlerResult.newResult().error(ExceptionWhileDataFetching(handlerParameters.path, exception, handlerParameters.sourceLocation)).build()
    }
}