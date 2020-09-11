package org.study.account.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime

enum class Role{
    Customer, Admin
}

sealed class User(open val id: String, open val name: String) {
    @JsonIgnore
    fun getBaseInfo() = "role: ${this.javaClass.simpleName}, id: `${this.id}`, name: `${this.name}`"

    override fun toString(): String = id
}

data class Customer(
        override val id: String,
        override val name: String,
        val createTime: DateTime
) : User(id, name)

data class Admin(
        override val id: String,
        override val name: String,
        val createTime: DateTime
) : User(id, name)