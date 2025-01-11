package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class RefreshTokenIsExpiredException(
    refreshToken: String
) : BaseClientException(
    reason = "Refresh token '$refreshToken' is already expired.",
    exceptionName = ClientExceptionName.REFRESH_TOKEN_EXPIRED,
    statusCode = HttpStatus.UNAUTHORIZED
)