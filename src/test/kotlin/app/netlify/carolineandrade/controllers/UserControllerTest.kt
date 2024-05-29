package app.netlify.carolineandrade.controllers

import app.netlify.carolineandrade.BaseControllerTest
import app.netlify.carolineandrade.plugins.configureRouting
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserControllerTest: BaseControllerTest() {
    private val userController: UserController = mockk()

    private val usersResponse = listOf(
        UserResponse(
            id = 1.toString(),
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "password",
        )
    )

    @BeforeTest
    fun setup() {
        moduleList = {
            configureRouting(userController)
        }
    }

    @Test
    fun `when get all users then should return a list with users`() = withBaseTestApplication {
        coEvery {
            userController.getAll()
        } returns usersResponse

        val call = handleRequest(HttpMethod.Get, "/users") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
        with(call) {
            assertEquals(HttpStatusCode.OK, response.status())

            val responseBody: List<UserResponse> = Json.decodeFromString(response.content!!)
            assertEquals(1, responseBody.size)
            assertEquals(usersResponse, responseBody)
        }
    }

    @Test
    fun `when post users then should create a user and return it`() = withBaseTestApplication {
        val userRequest = UserRequest(
            firstName = "Mary",
            lastName = "Lambert",
            email = "mary.lambert@example.com",
            password = "password",
        )
        val userResponse = UserResponse(
            id = 3.toString(),
            firstName = "Mary",
            lastName = "Lambert",
            email = "mary.lambert@example.com",
            password = "password",
        )

        coEvery {
            userController.create(any())
        } returns userResponse

        val call = handleRequest(HttpMethod.Post, "/users") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(userRequest))
        }

        with(call) {
            assertEquals(HttpStatusCode.Created, response.status())
            assertEquals("3", response.headers["id"])
        }
         coVerify(exactly = 1) { userController.create(userRequest) }
    }

    @Test
    fun `when get specific user id with wrong id then should return that user`() = withBaseTestApplication {
        val userResponse = usersResponse.first()
        coEvery {
            userController.getById(any())
        } returns userResponse

        val call = handleRequest(HttpMethod.Get, "/users/${userResponse.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
        with(call) {
            assertEquals(HttpStatusCode.OK, response.status())

            val responseBody: UserResponse = Json.decodeFromString(response.content!!)
            assertEquals(userResponse, responseBody)
        }
    }

    @Test
    fun `when put users then should update a user and return it`() = withBaseTestApplication {
        val userRequest = UserRequest(
            firstName = "Mary",
            lastName = "Lambert",
            email = "mary.lambert@example.com",
            password = "password2",
        )
        val id = "3"

        coEvery {
            userController.update(any(), any())
        } returns true

        val call = handleRequest(HttpMethod.Put, "/users/${id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(userRequest))
        }
        with(call) {
            assertEquals(HttpStatusCode.OK, response.status())

            val responseBody: Boolean = Json.decodeFromString(response.content!!)
            assertEquals(true, responseBody)
        }

        coVerify(exactly = 1) {
            userController.update(id, userRequest.copy(password = "password2"))
        }
    }

    @Test
    fun `when delete a user then should return ok`() = withBaseTestApplication {
        val userResponse = usersResponse.first()
        coEvery {
            userController.delete(any())
        } returns true

        val call = handleRequest(HttpMethod.Delete, "/users/${userResponse.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
        with(call) {
            assertEquals(HttpStatusCode.OK, response.status())

            val responseBody: Boolean = Json.decodeFromString(response.content!!)
            assertEquals(true, responseBody)
        }

        coVerify(exactly = 1) { userController.delete(userResponse.id.toInt()) }
    }
}
