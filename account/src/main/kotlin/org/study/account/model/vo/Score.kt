package org.study.account.model.vo

import org.joda.time.DateTime

data class Score(
        val id: String,
        val studentId: String,
        val courseId: String,
        val score: Float,
        val createTime: DateTime
)