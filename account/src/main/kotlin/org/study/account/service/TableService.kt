package org.study.account.service

import org.jetbrains.exposed.sql.SchemaUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.entity.Course
import org.study.account.model.entity.Score
import org.study.account.model.entity.Student
import org.study.account.model.entity.Teacher
import org.study.account.util.ExposedLogger

@Service
@Transactional
class TableService(
        val studentService: StudentService,
        val teacherService: TeacherService,
        val courseService: CourseService,
        val scoreService: ScoreService
) {
    val log = LoggerFactory.getLogger(this::class.java)

    fun createTables() {
        ExposedLogger.addLogger(log)

        SchemaUtils.drop(Student, Score, Course, Teacher)
        SchemaUtils.create(Student, Score, Course, Teacher)
    }

    fun insertTestData() {
        ExposedLogger.addLogger(log)

        val students = studentService.init()
        val teachers = teacherService.init()
        val courses = courseService.init(teachers)
        scoreService.init(students, courses)
    }
}