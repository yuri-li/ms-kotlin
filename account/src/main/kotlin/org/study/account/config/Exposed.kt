package org.study.account.config

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class Exposed {

    @Bean
    fun transactionManager(dataSource: HikariDataSource): SpringTransactionManager = SpringTransactionManager(dataSource)

    /*@Bean // PersistenceExceptionTranslationPostProcessor with proxyTargetClass=false, see https://github.com/spring-projects/spring-boot/issues/1844
    fun persistenceExceptionTranslationPostProcessor() = PersistenceExceptionTranslationPostProcessor()*/
}