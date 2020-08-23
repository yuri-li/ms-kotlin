package org.study.account.model.dto

import org.study.account.model.entity.Gender

data class Student(
        val name: String,
        val birthday: String,
        val gender: Gender
)