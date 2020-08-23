package org.study.account.model.vo

import com.expediagroup.graphql.annotations.GraphQLIgnore
import org.joda.time.DateTime

data class Score(
        val id: String,
        @GraphQLIgnore
        val studentId: String,
        val courseId: String,
        val score: Float,
        val createTime: DateTime
)