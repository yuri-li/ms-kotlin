package org.study.account.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import com.expediagroup.graphql.spring.operations.Query
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.study.account.exception.ErrorCodeException
import org.study.account.model.Admin
import org.study.account.model.Customer
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
     fun customException(flag: Boolean): String {
         if (flag) {
             throw ErrorCodeException("XXXXXXXXXXX", "故意出错")
         }
         return UUID.randomUUID().toString()
     }

    @GraphQLDescription("custom scalar: Kotlin Unit")
    fun customScalar() {
        log.info("test void ------")
    }

    @GraphQLDescription("Get all courses")
    fun courses(): List<Course> = courseService.findAll()

    suspend fun students(pageRequest: PageRequest): StudentPage {
        println(pageRequest.pageSize)
        val list = studentService.findAll()
        return StudentPage(pageSize = 3, list = list)
    }


    /*@GraphQLDescription("Returns message modified by the manually wired directive to force lowercase")
    @LowerCase
    fun forceLowercaseEcho(msg: String) = msg

    @GraphQLDescription("query that uses GraphQLContext context")
    fun contextualQuery(
            value: Int,
            context: MyGraphQLContext
    ): ContextualResponse = ContextualResponse(value, context.value)*/

    fun customer(context: OAuth2Context<Customer>, value: Int) = "context.user: ${context.user!!.getBaseInfo()}, 普通参数:${value}"
    fun customerDetail(context: OAuth2Context<Customer>, value: Int) = "(details) context.user: ${context.user!!.getBaseInfo()}, 普通参数:${value}"
    fun admin(context: OAuth2Context<Admin>, value: Int) = "context.user: ${context.user!!.getBaseInfo()}, 普通参数:${value}"
}


class OAuth2Context<T : User>(val token: String? = null, val user: T? = null) : GraphQLContext

@Component
class SecurityContextFactory : GraphQLContextFactory<OAuth2Context<User>> {
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): OAuth2Context<User> {
        val token = request.headers["token"]?.first()
        log.info("fetch token from headers: ${token}")

        return if (token == null) {
            OAuth2Context()
        } else if ("Customer" == token) {
            OAuth2Context<User>(token, Customer("c001", "cName001", DateTime.now()))
        } else if ("Admin" == token) {
            OAuth2Context<User>(token, Admin("a001", "aName001", DateTime.now()))
        } else {
            throw ErrorCodeException("invalid_token", "invalid token")
//            OAuth2Context()
        }
    }
}
