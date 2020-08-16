package org.study.account.model.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.springframework.stereotype.Repository
import org.study.account.model.dto.Score as Dto
import org.study.account.model.entity.Score as Entity
import org.study.account.model.vo.Score as Vo

@Repository
class ScoreDao {
    fun batchInsert(list: List<Dto>): List<Vo> = Entity.batchInsert(list) { dto ->
        this[Entity.studentId] = dto.studentId
        this[Entity.courseId] = dto.courseId
        this[Entity.score] = dto.score.toBigDecimal()
    }.map {
        rowMapper(it)
    }

    private fun rowMapper(it: ResultRow): Vo = Vo(
            it[Entity.id],
            it[Entity.studentId],
            it[Entity.courseId],
            it[Entity.score].toFloat(),
            it[Entity.createTime]
    )
}