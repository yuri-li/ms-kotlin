package org.study.account.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.ScoreDao
import org.study.account.model.vo.Course
import org.study.account.model.vo.Student
import org.study.account.model.dto.Score as Dto

@Service
@Transactional
class ScoreService(val dao: ScoreDao) {
    fun init(students: List<Student>, courses: List<Course>) {
        dao.batchInsert(listOf(
                Dto(students[0].id, courses[0].id, 80.toFloat()),
                Dto(students[0].id, courses[1].id, 90.toFloat()),
                Dto(students[0].id, courses[2].id, 99.toFloat()),
                Dto(students[1].id, courses[0].id, 70.toFloat()),
                Dto(students[1].id, courses[1].id, 60.toFloat()),
                Dto(students[1].id, courses[2].id, 80.toFloat()),
                Dto(students[2].id, courses[0].id, 80.toFloat()),
                Dto(students[2].id, courses[1].id, 80.toFloat()),
                Dto(students[2].id, courses[2].id, 80.toFloat()),
                Dto(students[3].id, courses[0].id, 50.toFloat()),
                Dto(students[3].id, courses[1].id, 30.toFloat()),
                Dto(students[3].id, courses[2].id, 20.toFloat()),
                Dto(students[4].id, courses[0].id, 76.toFloat()),
                Dto(students[4].id, courses[1].id, 87.toFloat()),
                Dto(students[5].id, courses[0].id, 31.toFloat()),
                Dto(students[5].id, courses[2].id, 34.toFloat()),
                Dto(students[6].id, courses[1].id, 89.toFloat()),
                Dto(students[6].id, courses[2].id, 98.toFloat())
        ))
    }

}