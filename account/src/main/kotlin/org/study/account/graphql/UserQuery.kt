package org.study.account.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.scalars.ID
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import com.expediagroup.graphql.spring.operations.Query
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.study.account.exception.ErrorCodeException
import org.study.account.model.User
import org.study.account.model.page.PageRequest
import org.study.account.model.page.StudentPage
import org.study.account.model.vo.Course
import org.study.account.service.CourseService
import org.study.account.service.StudentService
import java.util.*

@Component
class UserQuery(
        val courseService: CourseService,
        val studentService: StudentService
) : Query {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GraphQLDescription("flag=true -> throw exception; flag=false -> User")
    fun customException(flag: Boolean): User {
        if (flag) {
            throw ErrorCodeException("XXXXXXXXXXX", "故意出错")
        }
        return User(ID(UUID.randomUUID().toString()), "yuri", 18,"123@qq.com")
    }

    @GraphQLDescription("custom scalar: Kotlin Unit")
    fun customScalar() {
        log.info("test void")
    }

    @GraphQLDescription("Get all courses")
    fun courses(): List<Course> = courseService.findAll()

    suspend fun students(pageRequest: PageRequest): StudentPage {
        println(pageRequest.pageSize)
        val list = studentService.findAll()
        return StudentPage(pageSize = 3, list = list)
    }

    /*@GraphQLDescription("query that uses GraphQLContext context")
    fun contextualQuery(
            value: Int,
            context: MyGraphQLContext
    ): ContextualResponse = ContextualResponse(value, context.value)*/

}

/*
data class ContextualResponse(val passedInValue: Int, val contextValue: String)

class MyGraphQLContext(val value: String) : GraphQLContext

@Component
class MyGraphQLContextFactory : GraphQLContextFactory<MyGraphQLContext> {
    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse) =
            MyGraphQLContext(request.headers["token"]?.first() ?: throw ErrorCodeException("invalid_token", "required token"))
}*/
