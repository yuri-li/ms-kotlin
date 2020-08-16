package org.study.account.model.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.*

object Teacher : Table("t_teacher") {
    val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }
    val name = varchar("c_name", 20)
    val createTime = datetime("create_time").clientDefault { DateTime.now() }

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}