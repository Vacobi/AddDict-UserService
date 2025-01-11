package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class RefreshTokenIsInvalidException (
    refreshToken: String
) : BaseClientException(
    reason = "Refresh token '$refreshToken' is invalid.",
    exceptionName = ClientExceptionName.REFRESH_TOKEN_NOT_VALID,
    statusCode = HttpStatus.UNAUTHORIZED
)