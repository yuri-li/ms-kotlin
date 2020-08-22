package org.study.account.model.dataFetcher

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.study.account.model.vo.Course
import org.study.account.model.vo.Teacher
import java.util.concurrent.CompletableFuture

@Component("TeacherDataFetcher")
@Scope("prototype")
class TeacherDataFetcher : DataFetcher<CompletableFuture<Teacher>>, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun get(environment: DataFetchingEnvironment): CompletableFuture<Teacher> {
        val teacherId = environment.getSource<Course>().teacherId
        return environment
                .getDataLoader<String, Teacher>("teacherLoader")
                .load(teacherId)
    }
}