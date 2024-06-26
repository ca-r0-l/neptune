package app.netlify.carolineandrade

import app.netlify.carolineandrade.controllers.UserController
import app.netlify.carolineandrade.database.DatabaseSingleton
import app.netlify.carolineandrade.database.repositories.UserRepositoryImpl
import app.netlify.carolineandrade.plugins.configureRouting
import app.netlify.carolineandrade.plugins.configureSecurity
import app.netlify.carolineandrade.services.JwtService
import app.netlify.carolineandrade.services.users.UserServiceImpl
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

// psql -h db -U root -d neptune

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
    DatabaseSingleton.init(environment)

    val userRepository = UserRepositoryImpl()

    val userService = UserServiceImpl(userRepository)
    val jwtService = JwtService(this, userService)

    val userController = UserController(userService)

    configureSecurity(jwtService)
    configureRouting(userController, jwtService)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
