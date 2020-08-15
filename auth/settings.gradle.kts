pluginManagement {
    repositories {
        maven(url = "https://repo.spring.io/milestone")
        maven(url = "https://repo.spring.io/snapshot")
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "auth"