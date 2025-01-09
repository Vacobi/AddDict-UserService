package vstu.isd.userservice.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import vstu.isd.userservice.config.properties.JwtProperties

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder();

    @Bean
    fun authenticationProvider(
        userService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userService)
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration) : AuthenticationManager =
        config.authenticationManager
}