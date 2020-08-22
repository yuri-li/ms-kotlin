---
typora-root-url: assets
---

# 1 hello world

![image-20200727122625469](/image-20200727122625469.png)

# 2 Queries 

场景： 使用GraphQL模拟实现`SQL经典练习题（老师，学生，成绩）`

![preview](/view)

## 2.1 database

### 2.1.1 创建一个空的database

```
# 1 下载镜像
[root@JD3 ~]# docker image pull postgres:13-alpine

# 2 毕竟是数据库嘛，先挂个volume
[root@JD3 ~]# docker volume create db

# 3 启动容器
[root@JD3 ~]# docker container run -d -p 5432:5432 --name postgres -v db:/var/lib/postgresql/data  -e POSTGRES_PASSWORD=Yuri123. postgres:13-alpine

# 4 进入容器
[root@JD3 ~]# docker container exec -it postgres /bin/bash
bash-5.0# 

# 5 访问数据库
bash-5.0# psql -U postgres
psql (13beta3)
Type "help" for help.

# 6 清理环境
postgres=# DROP DATABASE IF EXISTS graphql;
DROP DATABASE
postgres=# \l
                                 List of databases
   Name    |  Owner   | Encoding |  Collate   |   Ctype    |   Access privileges   
-----------+----------+----------+------------+------------+-----------------------
 postgres  | postgres | UTF8     | en_US.utf8 | en_US.utf8 | 
 template0 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
(3 rows)

# 7 创建database
postgres=# CREATE DATABASE graphql;
CREATE DATABASE

# 8 使用最新创建的database
postgres=# \connect graphql
You are now connected to database "graphql" as user "postgres".

# 9 创建schema（user是关键字，所以，用双引号括起来）
graphql=# CREATE SCHEMA "user";
CREATE SCHEMA
```

### 2.1.2 微服务与数据库

![image-20200817023148338](/image-20200817023148338.png)



![image-20200817023718478](/image-20200817023718478.png)

### 2.1.3 插入测试数据

```
@Service
@Transactional
class TableService(
        val studentService: StudentService,
        val teacherService: TeacherService,
        val courseService: CourseService,
        val scoreService: ScoreService
) {
    val log = LoggerFactory.getLogger(this::class.java)

    fun insertTestData() {
        ExposedLogger.addLogger(log)

        val students = studentService.init()
        val teachers = teacherService.init()
        val courses = courseService.init(teachers)
        scoreService.init(students, courses)
    }
}
```

## 2.2 scalars

### ~~2.2.1 ID~~

`ID`与普通的String不同，`ID`特指主键

![image-20200819131732702](/image-20200819131732702.png)

### 2.2.2 Unit

```
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

@Configuration
class CustomScalarGeneratorHooks : SchemaGeneratorHooks {

    /**
     * Register additional GraphQL scalar types.
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        Unit::class -> graphqlUnitType
        else -> null
    }

    /**
     * Register Reactor Mono monad type.
     */
    override fun willResolveMonad(type: KType): KType = when (type.classifier) {
        Mono::class -> type.arguments.firstOrNull()?.type
        else -> type
    } ?: type

    /**
     * Exclude the Spring bean factory interface
     */
    override fun isValidSuperclass(kClass: KClass<*>): Boolean {
        return when {
            kClass.isSubclassOf(BeanFactoryAware::class) -> false
            else -> super.isValidSuperclass(kClass)
        }
    }
}

internal val graphqlUnitType = GraphQLScalarType.newScalar()
        .name("Unit")
        .description("Unit in Kotlin corresponds to the void in Java")
        .coercing(UnitCoercing)
        .build()

private object UnitCoercing : Coercing<Unit, Unit> {
    override fun parseValue(input: Any?): Unit = Unit

    override fun parseLiteral(input: Any?): Unit = Unit

    override fun serialize(dataFetcherResult: Any?): Unit = Unit
}
```

### 2.2.3 `org.joda.time.DateTime`

```
abstract class CustomCoercing<S, T> : Coercing<S, T> {
    final override fun serialize(dataFetcherResult: Any): T = write(dataFetcherResult as S)
    final override fun parseValue(input: Any): S = read(input as T)
    final override fun parseLiteral(input: Any): S = read(input as T)
    final override fun parseLiteral(input: Any?, variables: MutableMap<String, Any>?): S {
        return super.parseLiteral(input, variables)
    }

    abstract fun write(s: S): T
    abstract fun read(t: T): S
}

internal val graphqlDateTimeType = GraphQLScalarType.newScalar()
        .name("DateTime")
        .description("org.joda.time.DateTime")
        .coercing(object : CustomCoercing<DateTime, String>() {
            override fun write(s: DateTime): String = s.toString(toDatetimeFormatter())

            override fun read(t: String): DateTime = t.toDateTime()
        })
        .build()

```

## 2.3 嵌套查询

### 2.3.1 model

```
package org.study.account.model.vo

import com.expediagroup.graphql.annotations.GraphQLIgnore
import org.joda.time.DateTime

data class Course(
        val id: String,
        val name: String,
        @GraphQLIgnore
        val teacherId: String,
        val createTime: DateTime
) {
    lateinit var teacher: Teacher
}
```

### 2.3.2 Query

```
import org.study.account.model.vo.Course
import org.study.account.service.CourseService
import org.springframework.stereotype.Component
import com.expediagroup.graphql.spring.operations.Query

@Component
class UserQuery(val courseService: CourseService) : Query {
    @GraphQLDescription("Get all courses")
    fun courses(): List<Course> = courseService.findAll()
}
```

### 2.3.3 DataFetcher

```
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.study.account.model.vo.Course
import org.study.account.model.vo.Teacher
import java.util.concurrent.CompletableFuture

@Component
@Scope("prototype")
class TeacherDataFetcher : DataFetcher<CompletableFuture<Teacher>>, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun get(environment: DataFetchingEnvironment): CompletableFuture<Teacher> {
        val teacherId = environment.getSource<Course>().teacherId
        return environment
                .getDataLoader<String, Teacher>("teacherLoader")
                .load(teacherId)
    }
}
```


### 2.3.4 DataLoader

```
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
```

### 2.3.5 other settings

或许是因为`graphql-kotlin`不够完善，官网给出的例子，还有其他的配置。有点多...

请参考提交记录`N+1 problem`

## 2.4 pagination




# 3 Mutations



