package app.netlify.carolineandrade.database.repositories.tables

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val firstName: Column<String> = varchar("firstName", 50)
    val lastName: Column<String> = varchar("lastName", 50)
    val email: Column<String> = varchar("email", 50)
    val password: Column<String> = varchar("password", 50)
    val active: Column<Boolean> = bool("active")
    val createdAt: Column<LocalDateTime> = datetime("createdAt")
    val updatedAt: Column<LocalDateTime> = datetime("updatedAt")

    override val primaryKey = PrimaryKey(id)
}
