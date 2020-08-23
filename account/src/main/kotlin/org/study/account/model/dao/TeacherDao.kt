package org.study.account.model.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.study.account.model.dto.Teacher as Dto
import org.study.account.model.entity.Teacher as Entity
import org.study.account.model.vo.Teacher as Vo

@Repository
class TeacherDao {
    fun batchInsert(list: List<Dto>): List<Vo> = Entity.batchInsert(list) { dto ->
        this[Entity.name] = dto.name
    }.map { rowMapper(it) }

    private fun rowMapper(it: ResultRow): Vo = Vo(
            it[Entity.id],
            it[Entity.name],
            it[Entity.createTime]
    )

    fun findTeachers(ids: List<String>): List<Vo> = Entity.select { Entity.id inList ids }.map { rowMapper(it) }
}