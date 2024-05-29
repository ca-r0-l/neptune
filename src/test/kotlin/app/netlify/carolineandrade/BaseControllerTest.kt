package app.netlify.carolineandrade

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.server.application.install
import kotlinx.serialization.json.Json

abstract class BaseControllerTest {
    protected var moduleList: Application.() -> Unit = { }

    fun <R> withBaseTestApplication(test: TestApplicationEngine.() -> R) {
        withTestApplication({
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }

            moduleList()
        }) {
            test()
        }
    }
}
