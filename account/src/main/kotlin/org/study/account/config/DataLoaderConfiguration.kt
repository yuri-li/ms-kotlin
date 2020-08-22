package org.study.account.config

import com.expediagroup.graphql.spring.execution.DataLoaderRegistryFactory
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.study.account.model.vo.Teacher
import org.study.account.service.TeacherService
import java.util.concurrent.CompletableFuture

@Configuration
class DataLoaderConfiguration(val teacherService: TeacherService) {

    @Bean
    fun dataLoaderRegistryFactory(): DataLoaderRegistryFactory {
        return object : DataLoaderRegistryFactory {
            override fun generate(): DataLoaderRegistry {
                val registry = DataLoaderRegistry()
                val teacherLoader = DataLoader<String, Teacher> { ids ->
                    CompletableFuture.supplyAsync { teacherService.getTeachers(ids) }
                }
                registry.register("teacherLoader", teacherLoader)
                return registry
            }
        }
    }
}