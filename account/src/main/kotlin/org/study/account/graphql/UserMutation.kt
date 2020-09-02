package org.study.account.graphql

import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.study.account.model.User
import javax.validation.Valid

@Component
@Validated
class UserMutation : Mutation {
    fun addUser(@Valid request:User) = request.email
}
