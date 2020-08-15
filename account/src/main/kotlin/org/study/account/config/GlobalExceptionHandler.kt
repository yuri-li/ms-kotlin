package org.study.account.config

import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.slf4j.LoggerFactory
import org.study.account.exception.ErrorCodeException

class GlobalExceptionHandler : DataFetcherExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val exception = handlerParameters.exception

        log.error(exception.message)
        return DataFetcherExceptionHandlerResult.newResult().error(exception as ErrorCodeException).build()
    }
}