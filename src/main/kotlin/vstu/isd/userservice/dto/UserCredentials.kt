package vstu.isd.userservice.dto

import org.springframework.security.core.userdetails.User
import java.time.LocalDateTime

data class UserCredentials(
    val userId: Long,
    private val login: String,
    private val password: String,
    val updatedAt: LocalDateTime
) : User(
    login,
    password,
    true,
    true,
    true,
    true,
    emptyList()
)
