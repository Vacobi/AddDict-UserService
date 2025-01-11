package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class LoginIsNotUnique(
    login: String
) : BaseClientException(
    reason = "Login '$login' is not unique.",
    exceptionName = ClientExceptionName.LOGIN_IS_NOT_UNIQUE,
    statusCode = HttpStatus.CONFLICT
)