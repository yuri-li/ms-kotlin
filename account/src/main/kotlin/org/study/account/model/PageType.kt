package org.study.account.model

interface PageType
data class Page2<T>(
        val totalCount: Int, //总条数
        val pageSize: Int, //每页数量
        val totalPageNum: Int, //总页数
        val pageNum: Int, //当前页
        val list: List<T> //当前页的数据
) : PageType

data class CustomList<T>(val list: List<T>) : PageType