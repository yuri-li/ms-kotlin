package org.study.account.model

import com.expediagroup.graphql.scalars.ID
import org.study.account.model.directive.Lowercase
import javax.validation.constraints.Pattern

data class User(
        val id: ID,
        val name: String,
        val age: Int,
        @Lowercase
        @field:Pattern(regexp = EMAIL, message = "邮箱格式错误")
        val email: String
)
const val EMAIL = "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
