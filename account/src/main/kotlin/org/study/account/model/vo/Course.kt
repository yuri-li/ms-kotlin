package org.study.account.model.vo

import org.joda.time.DateTime

data class Course(
        val id:String,
        val name: String,
        val teacherId: String,
        val createTime: DateTime
)