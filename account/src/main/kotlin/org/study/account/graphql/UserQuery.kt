package org.study.account.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.scalars.ID
import com.expediagroup.graphql.spring.operations.Query
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.study.account.exception.ErrorCodeException
import org.study.account.model.CustomList
import org.study.account.model.PageType
import org.study.account.model.User
import org.study.account.model.vo.Course
import org.study.account.model.vo.Student
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
        return User(ID(UUID.randomUUID().toString()), "yuri", 18)
    }

    @GraphQLDescription("custom scalar: Kotlin Unit")
    fun customScalar() {
        log.info("test void")
    }

    @GraphQLDescription("Get all courses")
    fun courses(): List<Course> = courseService.findAll()

    suspend fun students() = studentService.findAll()

    fun whichHand(whichHand: String): BodyPart = when (whichHand) {
        "right" -> RightHand(12)
        else -> LeftHand("hello world")
    }
}

interface BodyPart

data class LeftHand(val field: String): BodyPart

data class RightHand(val property: Int): BodyPart