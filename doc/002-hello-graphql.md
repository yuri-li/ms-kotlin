---
typora-root-url: assets
---



# 1 为什么使用GraphQL

在回答这个问题之前，需要先了解 `Restful API`，因为`GraphQL`可以减少`API`的数量。

- `Restful API`，接口的参数与返回值都是固定的。接口如何定义的，就如何使用
- `GraphQL`相当于，前后端对接的`domain model`

# 2 演示

![image-20200724144433208](./image-20200724144433208.png)

# 3 编码

## 3.1 build.gradle.kts

```
plugins {
    idea
    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "org.study"
version = "1.0-SNAPSHOT"

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/milestone")
    maven(url = "https://repo.spring.io/snapshot")
    jcenter()
}

dependencies {
    val graphqlVersion = "3.4.1"
    val kotestVersion = "4.1.3"
    val springmockkVersion = "2.0.2"
    val logbackVersion = "6.4"

    implementation(kotlin("stdlib-jdk8"))
    implementation("com.expediagroup:graphql-kotlin-spring-server:$graphqlVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring-jvm:$kotestVersion")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
    withType<Wrapper> {
        gradleVersion = "6.5.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
```

## 3.2 application.yml

```
spring:
  application:
    name: account

graphql:
  packages:
    - "org.study.account.controller"
    - "org.study.account.model"
```

## 3.3 UserQuery

```
package org.study.account.controller

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.study.account.model.User

@Component
class UserQuery : Query {
    @GraphQLDescription("creates new user for given ID")
    fun userById(id: String): User{
        val user = User(id, "001", 18)
        log.info("create new user: ${user}")
        return user
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
```

## 3.4 运行

与普通的`springboot`项目启动的方式相同，略