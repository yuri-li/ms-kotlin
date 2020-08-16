package org.study.account.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.TeacherDao
import org.study.account.model.dto.Teacher as Dto

@Service
@Transactional
class TeacherService(val dao: TeacherDao) {
    fun init() = dao.batchInsert(listOf(
            Dto("张老师"),
            Dto("李老师"),
            Dto("王老师")
    ))
}