package app.netlify.carolineandrade.database.repositories.interfaces

import app.netlify.carolineandrade.models.User

interface UserRepository {
    suspend fun create(user: User): User?

    suspend fun getAll(): List<User>

    suspend fun getById(id: Int): User?

    suspend fun edit(id: Int, user: User): Boolean

    suspend fun delete(id: Int): Boolean

    suspend fun getByEmail(email: String): User?
}