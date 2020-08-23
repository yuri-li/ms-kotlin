package org.study.account.model.vo

import org.joda.time.DateTime
import org.study.account.model.entity.Gender

data class Student(
        val id: String,
        val name: String,
        val birthday: String,
        val gender: Gender,
        val createTime: DateTime
) {
    lateinit var scores: List<Score>
}