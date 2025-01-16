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
                responseCode = "403",
                description = """
                            User not created. It may be:  
                            Note with specified url is not found (api error code 100),  
                            Note is expired (api error code 101).
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
    @PostMapping("register")
    fun register(@RequestBody createUserRequestDto: CreateUserRequestDto) =
        userService.createUser(createUserRequestDto)
}