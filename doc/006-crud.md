---
typora-root-url: assets
---

# 1 hello world

![image-20200727122625469](/image-20200727122625469.png)

# 2 basic concepts

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

## 2.3 一对一嵌套查询

### 2.3.1 Model & Query

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

data class Course(
        val id: String,
        val name: String,
        @GraphQLIgnore
        val teacherId: String,
        val createTime: DateTime
) {
    lateinit var teacher: Teacher
}

data class Teacher(
        val id: String,
        val name: String,
        val createTime: DateTime
)
```

### 2.3.2 DataFetcher

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


### 2.3.3 DataLoader

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

### 2.3.4 other settings

或许是因为`graphql-kotlin`不够完善，官网给出的例子，还有其他的配置。有点多...

请参考提交记录`N+1 problem`

## 2.4 一对多嵌套查询

### 2.4.1 Model & Query

```
import org.study.account.model.vo.Student
import org.study.account.service.StudentService
import org.springframework.stereotype.Component
import com.expediagroup.graphql.spring.operations.Query

@Component
class UserQuery(val studentService: StudentService) : Query {
    fun students() : List<Student> = studentService.findAll()
}

data class Student(
        val id: String,
        val name: String,
        val birthday: String,
        val gender: Gender,
        val createTime: DateTime
) {
    lateinit var scores: List<Score>
}

data class Score(
        val id: String,
        @GraphQLIgnore
        val studentId: String,
        val courseId: String,
        val score: Float,
        val createTime: DateTime
)
```

### 2.4.2 DataFetcher

```
abstract class CustomDataFetcher<T> : DataFetcher<CompletableFuture<T>>, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    final override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }
}

@Component("ScoreDataFetcher")
@Scope("prototype")
class ScoreDataFetcher : CustomDataFetcher<List<Score>>() {
    override fun get(environment: DataFetchingEnvironment): CompletableFuture<List<Score>> {
        val studentId = environment.getSource<Student>().id
        return environment
                .getDataLoader<String, List<Score>>("scoreLoader")
                .load(studentId)
    }
}
```

### 2.4.3 DataLoader

```
org.study.account.config.DataLoaderConfiguration

...
register("scoreLoader", DataLoader<String, List<Score>> { studentIds ->
    CompletableFuture.supplyAsync {
        val scores: List<Score> = scoreService.findScoresByStudentIds(studentIds)
        studentIds.map { studentId -> scores.filter { it.studentId == studentId } }
    }
})
...
```

## 2.5 suspend & pagination

### 2.5.1 suspend

`suspend`是kotlin的关键字，与`coroutine`配套使用

```
suspend fun findAll(): List<Vo> = coroutineScope {
        Entity.selectAll().orderBy(Entity.createTime).map { rowMapper(it) }
    }
```

- `coroutine`相当于把线程作为上下文。一台普通的机器，开上万个`coroutine`都可以正常运行

- 创建`coroutine`的方法很多。不限于`coroutineScope{}`

- 必须使用`suspend`标注`coroutine`的代码，其他部分看起来跟`顺序执行`的代码没有区别

> 只是语法糖而已，肯定是框架帮我们做了剩下的工作

### 2.5.2 pagination

目前的版本，不支持泛型。

所以，每个需要分页显示的model，都需要定义一个具体的model。如下：

```
@Component
class UserQuery : Query{
  suspend fun students(pageRequest: PageRequest): StudentPage {
     ...
  }
}

data class StudentPage(
        override val pageSize: Int = 10,
        override val totalCount: Int = 1,
        override val list: List<Student> = emptyList()
) : Pageable<Student>()

data class Student(...)
```

### 2.5.3 自定义的pagination model

```
data class PageRequest(
        val pageNumber: Int = 1, //当前页
        val pageSize: Int = 10 //每页数量
)

open class Pageable<T>(
        open val pageSize: Int = 10, //每页数量
        open val totalCount: Int = 1, //总条数
        open val list: List<T> = emptyList() //当前页的数据
) {
    fun totalPageNumber() = if (totalCount % pageSize == 0) {
        totalCount / pageSize
    } else {
        totalCount / pageSize + 1
    }
}


```

## 2.6 context

```
@Component
class UserQuery : Query{
  @GraphQLDescription("query that uses GraphQLContext context")
    fun contextualQuery(
            value: Int,
            context: MyGraphQLContext
    ): ContextualResponse = ContextualResponse(value, context.myCustomValue)
}

class MyGraphQLContext(
  val myCustomValue: String, 
  val request: ServerHttpRequest, 
  val response: ServerHttpResponse, 
  var subscriptionValue: String? = null) : GraphQLContext

@Component
class MyGraphQLContextFactory : GraphQLContextFactory<MyGraphQLContext> {
    override suspend fun generateContext(
       request: ServerHttpRequest, 
       response: ServerHttpResponse
    ): MyGraphQLContext = MyGraphQLContext(
            myCustomValue = request.headers["token"]?.first() ?: 
               throw ErrorCodeException("invalid_token", "required token"),
            request = request,
            response = response,
            subscriptionValue = null
    )
}

@GraphQLDescription("simple response that contains value read from context")
data class ContextualResponse(val passedInValue: Int, val contextValue: String)
```

## 2.7 directive & validation

### 2.7.1 validation

```
1. dependency
val validationVersion = "0.0.3"
implementation("com.graphql-java:graphql-java-extended-validation:$validationVersion")

2. model
import com.expediagroup.graphql.scalars.ID
import javax.validation.constraints.Pattern

data class User(
        val id: ID,
        val name: String,
        val age: Int,
        @field:Pattern(regexp = EMAIL, message = "邮箱格式错误")
        val email: String
)
const val EMAIL = "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"

3. mutation
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.study.account.model.User
import javax.validation.Valid

@Component
@Validated
class UserMutation : Mutation {
    fun addUser(@Valid request:User) = request.email
}
```

### 2.7.2 directive







## 2.8 subscription

```
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Subscription
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random

@Component
class SimpleSubscription : Subscription {
    @GraphQLDescription("Returns a random number every second")
    fun counter(): Flux<Int> = Flux.interval(Duration.ofSeconds(1)).map { Random.nextInt() }

}
```

# 3 Introspection

# 4 OAuth2

![image-20200910180505874](/image-20200910180505874.png)



