package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus
import vstu.isd.userservice.dto.FindUserRequestDto

class UserNotFoundException(findUserRequest: FindUserRequestDto): BaseClientException (
    reason = "User with $findUserRequest was not found",
    exceptionName = ClientExceptionName.USER_NOT_FOUND,
    statusCode = HttpStatus.NOT_FOUND
)