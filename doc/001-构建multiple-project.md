---
typora-root-url: assets
---

# multiple-project

也可以称谓`composite-project`，一种项目构建的小技巧，这样做，可以将多个`project`放到同一个目录下。从目录结构上看，算是一个`project`。其实，只是分组，`project`之间只是逻辑上有关联。

> 接下来，用3个`project`模拟构建的过程

# 1 创建3个独立的project

就是普通的项目

- demo
- account
- auth

## 1.1 demo

### 1.1.1 settings.gradle.kts

```
pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
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
rootProject.name = "demo"
```

### 1.1.2 build.gradle.kts

```
plugins {
    idea
    kotlin("jvm") version "1.3.72"
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
```



### 1.1.3 执行wrapper task

指定gradle的版本。不同的版本，配置脚本的语法（API）有些区别

## 1.2 account & auth

这是两个可以运行的web项目，方便测试

### 1.2.1 settings.gradle.kts

此处的配置与demo相同，略

### 1.2.2 build.gradle.kts

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
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
```

### 1.2.3 构建项目

`intellij idea`构建的过程都是一样的，点一下`刷新`按钮就行了

![image-20200717222530366](/image-20200717222530366.png)

### 1.2.4 测试

写个controller，运行起来，看看效果

# 2 修改目录结构

## 2.1 demo

- 删除目录`build.grdle.kts, src`
- 修改settings.gradle.kts，在文件末尾添加一行代码

```
include( "account", "auth")
```



## 2.2 account & auth

将他们都挪到`demo`目录下

# 3 测试

- 通过demo，同时构建account & auth
- 同时启动account和auth，测试

