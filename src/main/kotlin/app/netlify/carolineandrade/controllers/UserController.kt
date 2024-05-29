package app.netlify.carolineandrade.controllers

import app.netlify.carolineandrade.models.User
import app.netlify.carolineandrade.services.users.interfaces.UserService
import kotlinx.serialization.Serializable

class UserController(
    private val userService: UserService,
) {
    suspend fun create(
        userRequest: UserRequest,
    ): UserResponse? =
        userService.create(
            userRequest.toModel()
        )?.toResponse()

    suspend fun getAll(): List<UserResponse> =
        userService
            .getAll()
            .map {
                it.toResponse()
            }

    suspend fun getById(
        id: Int,
    ): UserResponse =
        userService
        .getById(id)
        .takeIf { it != null }!!
        .toResponse()

    suspend fun delete(
        id: Int,
    ): Boolean =
        userService.delete(id)

    suspend fun update(
        id: String,
        userRequest: UserRequest,
    ): Boolean =
        userService.update(id.toInt(), userRequest.toModel())
}

private fun UserRequest.toModel(): User =
    User(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
    )

private fun User.toResponse(): UserResponse =
    UserResponse(
        id = id.toString(),
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
    )

@Serializable
data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

@Serializable
data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
