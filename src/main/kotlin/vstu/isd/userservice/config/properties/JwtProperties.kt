package vstu.isd.userservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "spring.jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpiration: Duration,
    val refreshTokenExpiration: Duration
) {
//    val accessTokenDuration: Duration
//        get() = Duration.parse("PT${accessTokenExpiration.uppercase(Locale.getDefault())}")
//
//    val refreshTokenDuration: Duration
//        get() = Duration.parse("PT${refreshTokenExpiration.uppercase(Locale.getDefault())}")
}
