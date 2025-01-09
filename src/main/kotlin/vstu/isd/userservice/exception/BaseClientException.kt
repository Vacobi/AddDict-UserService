package vstu.isd.userservice.exception

import org.springframework.http.HttpStatusCode

abstract class BaseClientException(
    reason: String,
    val exceptionName: ClientExceptionName,
    val statusCode: HttpStatusCode? = null
) : RuntimeException(reason) {
    open fun properties(): Map<String, Any> = LinkedHashMap()
}
