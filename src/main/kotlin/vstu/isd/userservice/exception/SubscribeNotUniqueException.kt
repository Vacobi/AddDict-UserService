package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class SubscribeNotUniqueException (
    private val subscriberId : Long,
    private val authorId : Long
) : BaseClientException(
    reason = "User with id: '$subscriberId' already subscribed to user with id '$authorId'",
    exceptionName = ClientExceptionName.SUBSCRIBE_NOT_UNIQUE,
    statusCode = HttpStatus.CONFLICT
)