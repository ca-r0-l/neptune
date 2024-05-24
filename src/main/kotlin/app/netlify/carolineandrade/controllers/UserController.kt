package app.netlify.carolineandrade.controllers

import app.netlify.carolineandrade.models.User
import app.netlify.carolineandrade.services.users.interfaces.UserCreationService
import kotlinx.serialization.Serializable
import java.util.UUID

class UserController(
    private val userCreationService: UserCreationService,
) {
    suspend fun create(
        userRequest: UserRequest,
    ): UserResponse? =
        userCreationService.create(
            userRequest.firstName,
            userRequest.lastName,
            userRequest.email,
            userRequest.password,
        )?.let {
            println("==>" + it)
            UserResponse(
                id = it.id.toString(),
                firstName = it.firstName,
                lastName = it.lastName,
                email = it.email,
                password = it.password,
            )
        }

    suspend fun getAll(): List<UserResponse> =
        userCreationService
            .getAll()
            .map {
                UserResponse(
                    id = it.id.toString(),
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email,
                    password = it.password,
                )
            }

    suspend fun getById(
        id: Int,
    ): UserResponse =
        userCreationService
            .getById(id)
            .takeIf { it != null }
            .let {
                UserResponse(
                    id = it!!.id.toString(),
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email,
                    password = it.password,
                )
            }
}

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
