package org.study.account.model.dao

import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.format.DateTimeFormat
import org.springframework.stereotype.Repository
import org.study.common.util.datePattern
import org.study.common.util.toDate
import org.study.account.model.dto.Student as Dto
import org.study.account.model.entity.Student as Entity
import org.study.account.model.vo.Student as Vo

@Repository
class StudentDao {
    fun insert(dto: Dto) = Entity.insert {
        it[name] = dto.name
        it[birthday] = dto.birthday.toDate()
        it[gender] = dto.gender
    }

    fun batchInsert(list: List<Dto>): List<Vo> = Entity.batchInsert(list) { dto ->
        this[Entity.name] = dto.name
        this[Entity.birthday] = dto.birthday.toDate()
        this[Entity.gender] = dto.gender
    }.map { rowMapper(it) }

    private fun rowMapper(it: ResultRow): Vo = Vo(
            it[Entity.id],
            it[Entity.name],
            it[Entity.birthday].toString(DateTimeFormat.forPattern(datePattern)),
            it[Entity.gender],
            it[Entity.createTime]
    )

    suspend fun findAll(): List<Vo> = coroutineScope {
        Entity.selectAll().orderBy(Entity.createTime).map { rowMapper(it) }
    }
}