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
    maven(url = "https://dl.bintray.com/kotlin/exposed")
}

dependencies {
    val graphqlVersion = "3.5.0"
    val kotestVersion = "4.1.3"
    val springmockkVersion = "2.0.2"
    val logbackVersion = "6.4"
    val exposedVersion = "0.25.1"
    val psqlVersion = "42.2.15"
    val hikariVersion = "3.4.5"
    val commonVersion: String by project

    implementation("org.study:common:$commonVersion")
    implementation("com.expediagroup:graphql-kotlin-spring-server:$graphqlVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackVersion")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposedVersion") {
        exclude(group = "com.h2database")
    }
    implementation("org.postgresql:postgresql:$psqlVersion")
    implementation("org.jetbrains.exposed:spring-transaction:$exposedVersion") {
        exclude(group = "com.h2database")
    }
    implementation("com.zaxxer:HikariCP:$hikariVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
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