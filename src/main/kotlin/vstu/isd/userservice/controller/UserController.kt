package vstu.isd.userservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.service.UserService

@RestController
@RequestMapping("api/v1/user")
class UserController(
    private val userService: UserService
) {

    @Operation(
        summary = "Register a new user.",
        description = "Allows registering users.",
        parameters = [
            Parameter(name = "createUserRequestDto", description = "Object with login and password of a new user.", required = true)
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "New user created successfully.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example = """
                        {
                            "id": 2,
                            "login": "some3"
                        }
                        """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = """
                            User not created. It may be:  
                            Group of validation exceptions (api error code 801),
                            Validation exception: Login isn't correct (api error code 802),  
                            Validation exception: Password isn't correct (api error code 803).
                            """,
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example = "No content"
                    )
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "User not created. User with same login already exists.",
                content = [Content(
                    mediaType = "application/json",
                    schema = io.swagger.v3.oas.annotations.media.Schema(
                        example = """
                            {
                                "type": "error",
                                "title": "Bad Request",
                                "status": 400,
                                "detail": "Group validation failed",
                                "instance": "/api/v1/user/register",
                                "date": "2025-01-17T00:29:42.8759012",
                                "api_error_code": 801,
                                "api_error_name": "GROUP_VALIDATION_EXCEPTION",
                                "args": {},
                                "errors": [
                                    {
                                        "api_error_code": 802,
                                        "api_error_name": "INVALID_LOGIN",
                                        "args": {},
                                        "detail": "Login must match rules: ^[a-zA-Z0-9_]{2,32}${'$'}"
                                    }
                                ]
                            }
                        """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error"
            )
        ]
    )
    @PostMapping("register")
    fun register(@RequestBody createUserRequestDto: CreateUserRequestDto) =
        userService.createUser(createUserRequestDto)
}