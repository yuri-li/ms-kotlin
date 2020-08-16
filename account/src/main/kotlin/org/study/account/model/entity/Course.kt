package org.study.account.model.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.UUID

object Course :Table("t_course"){
    val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }
    val name = varchar("c_name", 20)
    val teacherId = varchar("teacher_id", 36)
    val createTime = datetime("create_time").clientDefault { DateTime.now() }

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}