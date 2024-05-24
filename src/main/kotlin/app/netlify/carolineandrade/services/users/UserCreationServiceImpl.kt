package app.netlify.carolineandrade.services.users

import app.netlify.carolineandrade.database.repositories.interfaces.UserRepository
import app.netlify.carolineandrade.models.User
import app.netlify.carolineandrade.services.users.interfaces.UserCreationService
import java.util.UUID

class UserCreationServiceImpl(
    private val userRepository: UserRepository,
): UserCreationService {
    override suspend fun create(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): User? {
        val user = User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
        )

        return userRepository.create(user)
    }

    override suspend fun getAll(): List<User> =
        userRepository.getAll()

    override suspend fun getById(id: Int): User? =
        userRepository.getById(id)
}