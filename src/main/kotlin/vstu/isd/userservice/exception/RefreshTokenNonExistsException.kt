package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class RefreshTokenNonExistsException(
    token: String
) : BaseClientException(
    reason = "Token '${token}' was not found'",
    exceptionName = ClientExceptionName.REFRESH_TOKEN_NOT_FOUND,
    statusCode = HttpStatus.NOT_FOUND
)