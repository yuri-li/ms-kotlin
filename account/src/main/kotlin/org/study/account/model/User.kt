package org.study.account.model

import com.expediagroup.graphql.scalars.ID

data class User(
    val id: ID,
    val name: String,
    val age: Int
)