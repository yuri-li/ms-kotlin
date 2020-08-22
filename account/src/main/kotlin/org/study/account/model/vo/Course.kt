package org.study.account.model.vo

import com.expediagroup.graphql.annotations.GraphQLIgnore
import org.joda.time.DateTime

data class Course(
        val id: String,
        val name: String,
        @GraphQLIgnore
        val teacherId: String,
        val createTime: DateTime
) {
    lateinit var teacher: Teacher
}