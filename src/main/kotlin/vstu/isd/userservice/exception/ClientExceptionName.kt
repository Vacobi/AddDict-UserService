package vstu.isd.userservice.exception

enum class ClientExceptionName(
    val apiErrorCode: Int
) {
    VALIDATION_EXCEPTION(800),
    GROUP_VALIDATION_EXCEPTION(801),
    INVALID_LOGIN(802),
    INVALID_PASSWORD(803),
    LOGIN_IS_NOT_UNIQUE(804),
    INVALID_REFRESH_TOKEN(805),
    INVALID_FIND_USER_REQUEST(806),
    USER_NOT_FOUND(807)
}