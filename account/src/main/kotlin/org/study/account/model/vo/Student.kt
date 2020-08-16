package org.study.account.model.vo

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.study.account.model.entity.Sex

data class Student(
        val id: String,
        val name: String,
        val birthday: String,
        val sex: Sex,
        val createTime: DateTime
)