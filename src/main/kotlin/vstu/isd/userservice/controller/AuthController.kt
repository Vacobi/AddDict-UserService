package vstu.isd.userservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @Operation(
        summary = "Login into user's account.",
        description = "Allows login into account.",
        parameters = [
            Parameter(name = "authRequestDto", description = "Object with data to login in user's account.", required = true),
            Parameter(name = "response", description = "Entity in which adding cookie.", required = true)
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login successful.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example =
                            """
                            eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21lMTUiLCJpYXQiOjE3MzcwNjM1NTAsImV4cCI6MTczNzA2NDc1MCwidXNlcl9pZCI6Nn0.kYDXt0_5iAen6vhrz2K9f0Akx_O1M6VxmkLmHOSmmO8
                            """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = """
                            Login unsuccessful. It may be:
                            Incorrect login,
                            Incorrect password for this login
                            """,
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example = "No content"
                    )
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error"
            )
        ]
    )
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





    @Operation(
        summary = "Checks for valid token.",
        description = "Allows check the token for correctness (it's not expired and etc.).",
        parameters = [
            Parameter(name = "verifyRequest", description = "Token for check.", required = true)
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login successful.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example =
                            """
                            true
                            """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error"
            )
        ]
    )
    @PostMapping("/verify-access")
    fun verifyAccessToken(@RequestBody verifyRequest: VerifyAccessTokenRequestDto): Boolean {
        return authService.verifyAccessToken(verifyRequest)
    }





    @Operation(
        summary = "Refresh given token.",
        description = "Allows refresh token of the user. ",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Token was refreshed.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example =
                            """eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21lMTUiLCJpYXQiOjE3MzcwNjM5MzcsImV4cCI6MTczNzA2NTEzNywidXNlcl9pZCI6Nn0.UeFC3qbCFIUna49XvVGs-Zby8AJOu1PFfZGlV22nxi4"""
                    )
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error"
            )
        ]
    )
    @GetMapping("/refresh")
    fun refresh(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<String> {

        val newAccessToken = authService.refreshAccessToken(
            RefreshAccessTokenRequestDto(refreshToken)
        ).refreshedToken

        return ResponseEntity.ok(newAccessToken)
    }





    @Operation(
        summary = "Log out of user's account.",
        description = "Allows log out of user from account.",
        parameters = [
            Parameter(name = "response", description = "Entity in which adding cookie.", required = true)
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Log out successful.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example =
                            """
                            JSESSIONID
                            """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error"
            )
        ]
    )
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