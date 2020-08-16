package org.study.account.model.dao

import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import org.study.common.util.toDate
import org.study.account.model.entity.Student as Entity
import org.study.account.model.dto.Student as Dto

@Repository
class StudentDao {
    fun insert(dto:Dto) = Entity.insert {
        it[name] = dto.name
        it[birthday] = dto.birthday.toDate()
        it[sex] = dto.sex
    }
}