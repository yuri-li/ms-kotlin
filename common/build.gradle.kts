plugins {
    idea
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "org.study"
version = "1.0.1"

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
    val jodaVersion = "2.10.5"

    implementation(kotlin("stdlib-jdk8"))
    implementation("joda-time:joda-time:$jodaVersion")
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
                password = "Yuri2000."
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