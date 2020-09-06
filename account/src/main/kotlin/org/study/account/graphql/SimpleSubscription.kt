package org.study.account.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Subscription
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random

@Component
class SimpleSubscription : Subscription {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GraphQLDescription("Returns a random number every second")
    fun counter(): Flux<Int>{
        log.info("invoke subscription `counter`: `${Random.nextInt()}`")
        return Flux.interval(Duration.ofSeconds(1)).map { Random.nextInt() }
    }

}