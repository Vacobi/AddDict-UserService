package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class InvalidRefreshTokenException: BaseClientException(
    reason = "Refresh token is invalid. It's may be expired or non exists. Try to login.",
    exceptionName = ClientExceptionName.INVALID_REFRESH_TOKEN,
    statusCode = HttpStatus.UNAUTHORIZED
)