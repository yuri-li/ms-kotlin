package org.study.account.config

import com.expediagroup.graphql.spring.execution.DataLoaderRegistryFactory
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.study.account.model.vo.Score
import org.study.account.model.vo.Teacher
import org.study.account.service.ScoreService
import org.study.account.service.TeacherService
import java.util.concurrent.CompletableFuture

@Configuration
class DataLoaderConfiguration(
        val teacherService: TeacherService,
        val scoreService: ScoreService
) {

    @Bean
    fun dataLoaderRegistryFactory(): DataLoaderRegistryFactory {
        return object : DataLoaderRegistryFactory {
            override fun generate(): DataLoaderRegistry =
                    DataLoaderRegistry().register("teacherLoader", DataLoader<String, Teacher> { ids ->
                        CompletableFuture.supplyAsync { teacherService.findTeachers(ids) }
                    }).register("scoreLoader", DataLoader<String, List<Score>> { studentIds ->
                        CompletableFuture.supplyAsync {
                            val scores: List<Score> = scoreService.findScoresByStudentIds(studentIds)
                            studentIds.map { studentId -> scores.filter { it.studentId == studentId } }
                        }
                    })
        }
    }
}