package app.netlify.carolineandrade.services

import app.netlify.carolineandrade.services.users.interfaces.UserService
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import kotlinx.serialization.Serializable
import java.util.Date

class JwtService(
    private val application: Application,
    private val userService: UserService,
) {
    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withIssuer(issuer)
            .withAudience(audience)
            .build()

    suspend fun createJwtToken(
        loginRequest: LoginRequest,
    ): String? {
        val foundUser = userService.getByEmail(loginRequest.email)

        return if (foundUser != null && foundUser.password == loginRequest.password) {
            JWT
                .create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withClaim("email", foundUser.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        } else null
    }

    suspend fun customValidator(
        credential: JWTCredential,
    ): JWTPrincipal? {
        val email = extractEmail(credential)
        val foundUser = if (email != null) userService.getByEmail(email) else null

        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    private fun audienceMatches(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

    private fun extractEmail(credential: JWTCredential): String? =
        credential.payload.getClaim("email").asString()

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()
}

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)
