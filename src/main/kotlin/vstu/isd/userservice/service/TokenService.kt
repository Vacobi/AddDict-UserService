package vstu.isd.userservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import vstu.isd.userservice.config.properties.JwtProperties
import vstu.isd.userservice.dto.UserCredentials
import vstu.isd.userservice.util.toDate
import java.util.*

@Service
class TokenService(
    jwtProperties: JwtProperties
) {
    private val secret = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    fun generate(
        userDetails: UserCredentials,
        expiredAt: Date,
        jti: String? = null,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder().apply {
            if (jti != null) setId(jti)
            setSubject(userDetails.username)
            setIssuedAt(Date(System.currentTimeMillis()))
            setExpiration(expiredAt)
            val claims = mutableMapOf<String, Any>("user_id" to userDetails.userId).apply {
                putAll(additionalClaims)
            }
            addClaims(claims)
            signWith(secret)
        }.compact()

    fun isValidRefreshToken(token: String, userCredentials: UserCredentials): Boolean {
        return try {
            val claims = getClaims(token)

            if (!claims.containsMandatoryParams()) return false

            val login = claims.getLogin()
            val userId = claims.getUserId()
            val issuedAt = claims.issuedAt

            val credentialsUpdatedAt = userCredentials.updatedAt.toDate()

            return issuedAt.before(credentialsUpdatedAt) &&
                    login == userCredentials.username &&
                    userId == userCredentials.userId
        } catch (e: Exception) {
            false
        }
    }

    private fun Claims.containsMandatoryParams(): Boolean =
        containsKey("user_id") && getLogin() != null

    private fun Claims.getLogin(): String? = subject

    private fun Claims.getUserId(): Long? = get("user_id", Long::class.java)

    fun isValidToken(token: String): Boolean {
        return try {
            getClaims(token).containsMandatoryParams()
        } catch (e: Exception) {
            false
        }
    }

    private fun getClaims(token: String): Claims {
        val parser = Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()

        // also throw exception if token expired
        return parser.parseClaimsJws(token)
            .body
    }
}