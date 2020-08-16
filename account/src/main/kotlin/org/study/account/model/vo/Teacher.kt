package org.study.account.model.vo

import org.joda.time.DateTime

data class Teacher(
        val id: String,
        val name: String,
        val createTime: DateTime
)