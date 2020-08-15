# 1 场景描述

`GraphQL`开放的`API`是一个可以交互的页面，`url`地址有点陌生。

怎么让服务启动后，自动打开这个网址呢？

# 2 如何实现？

```
fun String.openUrl() {
    //Get the name of the operating system
    val osName = System.getProperty("os.name")
    when {
        osName.startsWith("Mac OS") -> Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", *arrayOf<Class<*>>(String::class.java)).invoke(null, *arrayOf<Any>(this))
        osName.startsWith("Windows") -> Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler ${this}")
        else -> {
            var flag = true
            // Unix or Linux
            arrayOf("firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape").forEach {
                if (Runtime.getRuntime().exec(arrayOf("which", it)).waitFor() == 0) {
                    flag = false
                    Runtime.getRuntime().exec(arrayOf<String>(it, this))
                    return
                }
            }
            if (flag) throw Exception("Could not find web browser")
        }
    }
}
```

# 3 publish to private repository

```
plugins {
    idea
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "org.study"
version = "1.0.0"

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
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    withType<Wrapper> {
        gradleVersion = "6.5.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}
publishing {
    repositories {
        maven {
            setUrl("http://116.196.123.19:8081/repository/ms-kotlin")
            credentials {
                username = "admin"
                password = "yuri123."
            }
        }
    }
    publications {
        register("mavenKotlin", MavenPublication::class) {
            from(components["kotlin"])
            artifact(sourcesJar.get())
        }
    }
}
```

# 4 Load dependencies from private Repository

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
    maven(url = "http://116.196.123.19:8081/repository/ms-kotlin")
}

dependencies {
    val graphqlVersion = "3.4.1"
    val kotestVersion = "4.1.3"
    val springmockkVersion = "2.0.2"
    val logbackVersion = "6.4"

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.study:common:1.0.0")
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

# 5 配置频繁修改的version

## 5.1 gradle.properties

```
commonVersion=1.0.0
```

## 5.2 build.gradle.kts

```
dependencies{
	...
	val commonVersion:String by project
	
	implementation("org.study:common:$commonVersion")
}
```

# 6 调用common的函数

```
package org.study.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.study.common.util.openUrl

@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
    "http://localhost:8080/playground".openUrl()
}
```



# 7 测试

启动`account`，观察浏览器是否自动打开网址`http://localhost:8080/playground`

