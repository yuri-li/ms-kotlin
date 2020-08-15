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