package vstu.isd.userservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import java.lang.Exception

@Configuration
@EnableWebSecurity
class SecurityRequestsConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors().and() // Включаем CORS
            .csrf().disable() // Отключаем CSRF, если не требуется
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/api/v1/auth/**").permitAll() // Разрешаем доступ к auth эндпоинтам
                    .anyRequest().authenticated() // Требуем аутентификацию для всех остальных запросов
            }
        return http.build()
    }
}