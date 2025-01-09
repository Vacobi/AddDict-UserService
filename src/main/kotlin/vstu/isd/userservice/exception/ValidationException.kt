package vstu.isd.userservice.exception

import org.springframework.http.HttpStatus

open class ValidationException(
    reason: String,
    exceptionName: ClientExceptionName = ClientExceptionName.VALIDATION_EXCEPTION,
) : BaseClientException(reason, exceptionName, HttpStatus.BAD_REQUEST)