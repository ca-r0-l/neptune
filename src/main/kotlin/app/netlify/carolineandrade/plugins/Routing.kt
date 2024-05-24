package app.netlify.carolineandrade.plugins

import app.netlify.carolineandrade.controllers.UserController
import app.netlify.carolineandrade.controllers.UserRequest
import app.netlify.carolineandrade.controllers.UserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(
    userController: UserController,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/users") {
            get {
                val users: List<UserResponse> = userController.getAll()
                call.respond(users)
            }

            get("/{id}") {
                val id = call.parameters["id"] ?:
                    return@get call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)

                val users: UserResponse = userController.getById(id.toInt())
                call.respond(users)
            }

            post {
                val userRequest = call.receive<UserRequest>()
                val user: UserResponse? = userController.create(userRequest)
                call.response.header(
                    "id",
                    user?.id ?: "",
                )
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
