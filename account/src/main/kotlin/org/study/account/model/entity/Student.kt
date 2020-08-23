package org.study.account.model.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.UUID

object Student : Table(name = "t_student") {
    val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }
    val name = varchar("c_name", 20)
    val birthday = date("birthday")
    val gender = enumerationByName("gender", 10, Gender::class)
    val createTime = datetime("create_time").clientDefault { DateTime.now() }

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

enum class Gender {
    Male, Female, Neutral
}