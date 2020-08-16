package org.study.account.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.CourseDao
import org.study.account.model.vo.Teacher
import org.study.account.model.dto.Course as Dto
import org.study.account.model.vo.Course as Vo

@Service
@Transactional
class CourseService(val dao: CourseDao) {
    fun init(teachers: List<Teacher>): List<Vo> {
        val courseNames = listOf("语文", "数学", "英语")
        val dto = courseNames.zip(teachers).map {
            Dto(it.first, it.second.id)
        }
        return dao.batchInsert(dto)
    }
}