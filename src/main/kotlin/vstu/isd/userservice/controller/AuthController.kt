package vstu.isd.userservice.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vstu.isd.userservice.dto.AuthRequestDto
import vstu.isd.userservice.dto.RefreshAccessTokenRequestDto
import vstu.isd.userservice.dto.VerifyAccessTokenRequestDto
import vstu.isd.userservice.service.AuthService

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PatchMapping("/login")
    fun login(@RequestBody authRequestDto: AuthRequestDto, response: HttpServletResponse): ResponseEntity<String> {

        val authResponse = authService.authenticate(authRequestDto)

        val cookie = Cookie("refresh_token", authResponse.refreshToken).apply {
            isHttpOnly = true
            secure = true
        }
        response.addCookie(cookie)

        return ResponseEntity.ok(authResponse.accessToken)
    }

    @PostMapping("/verify-access")
    fun verifyAccessToken(@RequestBody verifyRequest: VerifyAccessTokenRequestDto): Boolean {
        return authService.verifyAccessToken(verifyRequest)
    }

    @GetMapping("/refresh")
    fun refresh(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<String> {

        val newAccessToken = authService.refreshAccessToken(
            RefreshAccessTokenRequestDto(refreshToken)
        ).refreshedToken

        return ResponseEntity.ok(newAccessToken)
    }

    @PatchMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {

        val cookie = Cookie("refresh_token", "").apply {
            maxAge = 0
            path = "/api/v1/auth"
            isHttpOnly = true
        }

        response.addCookie(cookie)

        return ResponseEntity.ok().build()
    }
}