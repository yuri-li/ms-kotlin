package org.study.account.model.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.springframework.stereotype.Repository
import org.study.account.model.dto.Course as Dto
import org.study.account.model.entity.Course as Entity
import org.study.account.model.vo.Course as Vo

@Repository
class CourseDao {
    fun batchInsert(list: List<Dto>): List<Vo> = Entity.batchInsert(list) { dto ->
        this[Entity.name] = dto.name
        this[Entity.teacherId] = dto.teacherId
    }.map { rowMapper(it) }

    private fun rowMapper(it: ResultRow): Vo = Vo(
            it[Entity.id],
            it[Entity.name],
            it[Entity.teacherId],
            it[Entity.createTime]
    )
}