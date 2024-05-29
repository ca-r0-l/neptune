package app.netlify.carolineandrade.services.users

import app.netlify.carolineandrade.database.repositories.interfaces.UserRepository
import app.netlify.carolineandrade.models.User
import app.netlify.carolineandrade.services.users.interfaces.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
): UserService {
    override suspend fun create(
        user: User,
    ): User? =
        userRepository.create(user)

    override suspend fun getAll(): List<User> =
        userRepository.getAll()

    override suspend fun getById(id: Int): User? =
        userRepository.getById(id)

    override suspend fun delete(id: Int): Boolean =
        userRepository.delete(id)

    override suspend fun update(
        id: Int,
        user: User,
    ): Boolean =
        userRepository.edit(id, user)
}
