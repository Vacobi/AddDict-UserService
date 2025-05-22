package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class SubscribeNotExistsException (
    private val id : Long
) : BaseClientException(
    reason = "Subscribe with id: '$id' not found.",
    exceptionName = ClientExceptionName.SUBSCRIBE_NOT_FOUND,
    statusCode = HttpStatus.NOT_FOUND
)