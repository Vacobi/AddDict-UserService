package vstu.isd.userservice.mapper

import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.dto.FindUserRequestDto
import vstu.isd.userservice.dto.UserCredentials
import vstu.isd.userservice.dto.UserDto
import vstu.isd.userservice.entity.User

fun User.toDto() = UserDto(id!!, login!!)

fun CreateUserRequestDto.toEntity() = User(null, login, password)

fun User.toUserCredentials() =
    UserCredentials(id!!, login!!, password!!, credentialsUpdatedAt)

fun FindUserRequestDto.toEntity() = User(id, login, null)