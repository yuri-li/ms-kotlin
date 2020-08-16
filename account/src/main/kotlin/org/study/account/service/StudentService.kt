package org.study.account.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.study.account.model.dao.StudentDao
import org.study.account.model.entity.Sex
import org.study.account.model.dto.Student as Dto

@Service
@Transactional
class StudentService(val dao: StudentDao) {
    fun init() = dao.batchInsert(listOf(
            Dto("赵雷" , "1990-01-01" , Sex.Male),
            Dto("钱电" , "1990-12-21" , Sex.Male),
            Dto("孙风" , "1990-05-20" , Sex.Male),
            Dto("李云" , "1990-08-06" , Sex.Male),
            Dto("周梅" , "1991-12-01" , Sex.Female),
            Dto("吴兰" , "1992-03-01" , Sex.Female),
            Dto("郑竹" , "1989-07-01" , Sex.Female),
            Dto("王菊" , "1990-01-20" , Sex.Female)

    ))
}