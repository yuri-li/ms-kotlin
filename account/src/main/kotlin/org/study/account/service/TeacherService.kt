package org.study.account.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.TeacherDao
import org.study.account.util.ExposedLogger
import org.study.account.model.dto.Teacher as Dto
import org.study.account.model.vo.Teacher as Vo

@Service
@Transactional
class TeacherService(val dao: TeacherDao) {
    val log = LoggerFactory.getLogger(this::class.java)

    fun init() = dao.batchInsert(listOf(
            Dto("张老师"),
            Dto("李老师"),
            Dto("王老师")
    ))

    fun getTeachers(ids: List<String>): List<Vo> {
        ExposedLogger.addLogger(log)

        return dao.getTeachers(ids)
    }
}