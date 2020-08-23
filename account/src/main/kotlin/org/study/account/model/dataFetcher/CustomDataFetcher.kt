package org.study.account.model.dataFetcher

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.study.account.model.vo.Course
import org.study.account.model.vo.Score
import org.study.account.model.vo.Student
import org.study.account.model.vo.Teacher
import java.util.concurrent.CompletableFuture

abstract class CustomDataFetcher<T> : DataFetcher<CompletableFuture<T>>, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    final override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }
}

@Component("TeacherDataFetcher")
@Scope("prototype")
class TeacherDataFetcher : CustomDataFetcher<Teacher>() {
    override fun get(environment: DataFetchingEnvironment): CompletableFuture<Teacher> {
        val teacherId = environment.getSource<Course>().teacherId
        return environment
                .getDataLoader<String, Teacher>("teacherLoader")
                .load(teacherId)
    }
}


@Component("ScoreDataFetcher")
@Scope("prototype")
class ScoreDataFetcher : CustomDataFetcher<List<Score>>() {
    override fun get(environment: DataFetchingEnvironment): CompletableFuture<List<Score>> {
        val studentId = environment.getSource<Student>().id
        return environment
                .getDataLoader<String, List<Score>>("scoreLoader")
                .load(studentId)
    }
}
