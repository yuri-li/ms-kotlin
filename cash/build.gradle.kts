plugins {
    idea
    id("org.springframework.boot") version "2.3.2.RELEASE"
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
    mavenLocal()
    mavenCentral()
    maven(url = "https://repo.spring.io/milestone")
    maven(url = "https://repo.spring.io/snapshot")
    jcenter()
    maven(url = "http://116.196.123.19:8081/repository/ms-kotlin")
}

dependencies {
    val graphqlVersion = "3.5.0"
    val logbackVersion = "6.4"
    val commonVersion: String by project

    implementation("org.study:common:$commonVersion")
    implementation("com.expediagroup:graphql-kotlin-spring-server:$graphqlVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackVersion")
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