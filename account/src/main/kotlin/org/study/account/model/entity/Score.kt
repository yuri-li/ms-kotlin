package org.study.account.model.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.UUID

object Score : Table(name = "t_score") {
    val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }
    val courseId = varchar("course_id", 36)
    val score = decimal("score", 3, 2)
    val createTime = datetime("create_time").clientDefault { DateTime.now() }

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}