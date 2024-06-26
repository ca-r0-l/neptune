package app.netlify.carolineandrade.services.users.interfaces

import app.netlify.carolineandrade.models.User

interface UserService {
    suspend fun create(
        user: User,
    ): User?

    suspend fun getAll(): List<User>

    suspend fun getById(
        id: Int,
    ): User?

    suspend fun delete(
        id: Int,
    ): Boolean

    suspend fun update(
        id: Int,
        user: User,
    ): Boolean

    suspend fun getByEmail(
        email: String,
    ): User?
}
