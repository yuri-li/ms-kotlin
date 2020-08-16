package org.study.account.graphql

import com.expediagroup.graphql.spring.operations.Query
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.study.account.exception.ErrorCodeException
import org.study.account.model.User
import java.util.*

@Component
class UserQuery : Query {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun me(flag: Boolean): User {
        if (flag) {
            throw ErrorCodeException("XXXXXXXXXXX", "故意出错")
        }
        return User(UUID.randomUUID().toString(), "yuri", 18)
    }

    fun noContent() {
        log.info("test void")
    }
}