package app.netlify.carolineandrade.services.users.interfaces

import app.netlify.carolineandrade.models.User

interface UserCreationService {
    suspend fun create(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): User?

    suspend fun getAll(): List<User>

    suspend fun getById(
        id: Int,
    ): User?
}
