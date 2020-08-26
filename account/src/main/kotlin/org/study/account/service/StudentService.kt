package org.study.account.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.StudentDao
import org.study.account.model.entity.Gender
import org.study.account.util.ExposedLogger
import org.study.account.model.dto.Student as Dto
import org.study.account.model.vo.Student as Vo

@Service
@Transactional
class StudentService(val dao: StudentDao) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun init() = dao.batchInsert(listOf(
            Dto("赵雷" , "1990-01-01" , Gender.Male),
            Dto("钱电" , "1990-12-21" , Gender.Male),
            Dto("孙风" , "1990-05-20" , Gender.Male),
            Dto("李云" , "1990-08-06" , Gender.Male),
            Dto("周梅" , "1991-12-01" , Gender.Female),
            Dto("吴兰" , "1992-03-01" , Gender.Female),
            Dto("郑竹" , "1989-07-01" , Gender.Female),
            Dto("王菊" , "1990-01-20" , Gender.Female)

    ))

    suspend fun findAll(): List<Vo> {
        ExposedLogger.addLogger(log)

        return dao.findAll()
    }
}