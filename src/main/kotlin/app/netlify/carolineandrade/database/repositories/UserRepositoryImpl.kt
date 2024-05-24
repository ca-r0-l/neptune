package app.netlify.carolineandrade.database.repositories

import app.netlify.carolineandrade.database.DatabaseSingleton.dbQuery
import app.netlify.carolineandrade.database.repositories.interfaces.UserRepository
import app.netlify.carolineandrade.database.repositories.tables.UserTable
import app.netlify.carolineandrade.database.repositories.tables.UserTable.id
import app.netlify.carolineandrade.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl: UserRepository {
    private fun resultRowToUser(row: ResultRow) =
        User(
            id = row[UserTable.id],
            firstName = row[UserTable.firstName],
            lastName = row[UserTable.lastName],
            email = row[UserTable.email],
            password = row[UserTable.password],
            active = row[UserTable.active],
            createdAt = row[UserTable.createdAt],
            updatedAt = row[UserTable.updatedAt],
        )

    override suspend fun create(user: User): User? = dbQuery {
        val insertStatement = UserTable.insert {
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[email] = user.email
            it[password] = user.password
            it[active] = user.active
            it[createdAt] = user.createdAt
            it[updatedAt] = user.updatedAt
        }
        insertStatement
            .resultedValues
            ?.singleOrNull()
            ?.let(::resultRowToUser)
    }

    override suspend fun edit(user: User): Boolean = dbQuery {
        UserTable.update({ id eq user.id!! }) {
            it[id] = user.id!!
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[email] = user.email
            it[password] = user.password
            it[active] = user.active
            it[createdAt] = user.createdAt
            it[updatedAt] = user.updatedAt
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }

    override suspend fun getAll(): List<User> = dbQuery {
        UserTable
            .selectAll()
            .map(::resultRowToUser)
    }

    override suspend fun getById(id: Int): User? = dbQuery {
        UserTable
            .select { UserTable.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }
}
