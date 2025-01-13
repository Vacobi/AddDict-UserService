package vstu.isd.userservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.service.UserService

@RestController
@RequestMapping("api/v1/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun register(createUserRequestDto: CreateUserRequestDto) =
        userService.createUser(createUserRequestDto)
}