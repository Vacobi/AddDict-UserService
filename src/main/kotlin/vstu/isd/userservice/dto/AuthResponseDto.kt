package vstu.isd.userservice.dto

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String
)
