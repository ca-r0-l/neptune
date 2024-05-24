package app.netlify.carolineandrade.database

import app.netlify.carolineandrade.database.repositories.tables.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun hikari(
    url: String,
    user: String,
    password: String,
    pool: Int,
): HikariDataSource {
    val config = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = url
        this.username = user
        this.password = password
        maximumPoolSize = pool
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }
    config.validate()
    return HikariDataSource(config)
}

object DatabaseSingleton {
    fun init(environment: ApplicationEnvironment) {
        val database = Database.connect(
            hikari(
                url = environment.config.property("db.url").getString(),
                user = environment.config.property("db.user").getString(),
                password = environment.config.property("db.password").getString(),
                pool = environment.config.property("db.pool").getString().toInt(),
            )
        )

        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
