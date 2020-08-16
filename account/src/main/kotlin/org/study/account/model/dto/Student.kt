package org.study.account.model.dto

import org.study.account.model.entity.Sex

data class Student(
        val name: String,
        val birthday: String,
        val sex: Sex
)