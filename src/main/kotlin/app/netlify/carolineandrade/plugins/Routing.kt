package app.netlify.carolineandrade.plugins

import app.netlify.carolineandrade.controllers.UserController
import app.netlify.carolineandrade.controllers.UserRequest
import app.netlify.carolineandrade.controllers.UserResponse
import app.netlify.carolineandrade.controllers.authRoute
import app.netlify.carolineandrade.services.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(
    userController: UserController,
    jwtService: JwtService,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/auth") {
            authRoute(jwtService)
        }

        route("/users") {
            authenticate {
                get {
                    val users: List<UserResponse> = userController.getAll()
                    call.response.header(
                        "Content-Type",
                        "application/json",
                    )
                    call.respond(users)
                }

                get("/{id}") {
                    val id = call.parameters["id"] ?:
                        return@get call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)
                    call.response.header(
                        "Content-Type",
                        "application/json",
                    )
                    val users: UserResponse = userController.getById(id.toInt())
                    call.respond(users)
                }

                delete("/{id}") {
                    val id = call.parameters["id"] ?:
                    return@delete call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)

                    val deleted: Boolean = userController.delete(id.toInt())

                    call.response.header(
                        "Content-Type",
                        "application/json",
                    )

                    call.respond(deleted)
                }

                put("/{id}") {
                    val id = call.parameters["id"] ?:
                    return@put call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)
                    val userRequest = call.receive<UserRequest>()

                    val updated: Boolean = userController.update(id, userRequest)

                    call.response.header(
                        "Content-Type",
                        "application/json",
                    )

                    call.respond(updated)
                }
            }

            post {
                val userRequest = call.receive<UserRequest>()
                val user: UserResponse? = userController.create(userRequest)
                call.response.header(
                    "id",
                    user?.id ?: "",
                )
                call.response.header(
                    "Content-Type",
                    "application/json",
                )
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
