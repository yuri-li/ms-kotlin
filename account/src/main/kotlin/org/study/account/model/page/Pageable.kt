package org.study.account.model.page

import org.study.account.model.vo.Student

data class PageRequest(
        val pageNumber: Int = 1, //当前页
        val pageSize: Int = 10 //每页数量
)

open class Pageable<T>(
        open val pageSize: Int = 10, //每页数量
        open val totalCount: Int = 1, //总条数
        open val list: List<T> = emptyList() //当前页的数据
) {
    fun totalPageNumber() = if (totalCount % pageSize == 0) {
        totalCount / pageSize
    } else {
        totalCount / pageSize + 1
    }
}

data class StudentPage(
        override val pageSize: Int = 10,
        override val totalCount: Int = 1,
        override val list: List<Student> = emptyList()
) : Pageable<Student>()