package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

class SubscribeNotExistsException : BaseClientException {
    constructor(id: Long) : super(
        reason = "Subscribe with id: '$id' not found.",
        exceptionName = ClientExceptionName.SUBSCRIBE_NOT_FOUND,
        statusCode = HttpStatus.NOT_FOUND
    )

    constructor(subscriberId: Long, authorId: Long) : super(
        reason = "Subscribe between subscriberId: '$subscriberId' and authorId: '$authorId' not found.",
        exceptionName = ClientExceptionName.SUBSCRIBE_NOT_FOUND,
        statusCode = HttpStatus.NOT_FOUND
    )
}