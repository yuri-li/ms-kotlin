package org.study.account.config

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.result.view.ViewResolver
import org.study.account.exception.ErrorCodeDataFetchingGraphQLError
import org.study.account.exception.ErrorCodeException

@Configuration
class GlobalExceptionHandler : DataFetcherExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val exception = handlerParameters.exception

        log.error(exception.message)

        if (exception is ErrorCodeException) {
            return response(exception)
        }
        if (exception is GraphQLError) {
            return DataFetcherExceptionHandlerResult.newResult().error(exception).build()
        }

        return DataFetcherExceptionHandlerResult.newResult().error(ExceptionWhileDataFetching(handlerParameters.path, exception, handlerParameters.sourceLocation)).build()
    }
}
internal fun response(exception: ErrorCodeException) =
        DataFetcherExceptionHandlerResult.newResult().error(ErrorCodeDataFetchingGraphQLError(exception)).build()

@Configuration
@Order(-2)
class GlobalErrorHandlerConfiguration(
        errorAttributes: ErrorAttributes,
        resourceProperties: ResourceProperties,
        applicationContext: ApplicationContext,
        viewResolversProvider: ObjectProvider<List<ViewResolver>>,
        serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resourceProperties, applicationContext) {

    init {
        setViewResolvers(viewResolversProvider.getIfAvailable { emptyList() })
        setMessageWriters(serverCodecConfigurer.writers)
        setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(e: ErrorAttributes): RouterFunction<ServerResponse> =
            RouterFunctions.route(RequestPredicates.all(), HandlerFunction<ServerResponse> { request ->
                ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue(
                                ErrorCodeDataFetchingGraphQLError(request.exchange().attributes["org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR"]!! as ErrorCodeException)
                        ))
            })
}
