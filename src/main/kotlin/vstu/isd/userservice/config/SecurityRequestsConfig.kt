package vstu.isd.userservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityRequestsConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll()
                    .requestMatchers(HttpMethod.PATCH, "api/v1/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "api/v1/auth/verify-access").permitAll()
                    .requestMatchers(HttpMethod.GET, "api/v1/auth/refresh").permitAll()
                    .requestMatchers(HttpMethod.PATCH, "api/v1/auth/logout").permitAll()
                requests.anyRequest().authenticated()
            }
        return http.build()
    }

}