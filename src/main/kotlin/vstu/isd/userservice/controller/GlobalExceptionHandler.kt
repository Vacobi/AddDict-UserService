package vstu.isd.userservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import vstu.isd.userservice.exception.BaseClientException
import vstu.isd.userservice.exception.GroupValidationException
import java.net.URI
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GroupValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleGroupValidationException(groupExp: GroupValidationException): ErrorResponseException {
        val problemDetail = buildBaseClientExceptionProblemDetail(groupExp).apply {
            setProperty("errors", buildGroupValidationExceptionsErrors(groupExp))
        }
        return ErrorResponseException(groupExp.statusCode!!, problemDetail, groupExp)
    }

    private fun buildBaseClientExceptionProblemDetail(baseClientException: BaseClientException): ProblemDetail {
        val problemDetail = buildBaseProblemDetail(
            baseClientException.message ?: "Unknown error",
            baseClientException.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR
        )
        buildBaseClientExceptionProblemDetailProperties(baseClientException)
            .forEach { (key, value) -> problemDetail.setProperty(key, value) }
        return problemDetail
    }

    private fun buildBaseProblemDetail(reason: String, httpStatusCode: HttpStatusCode): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, reason)
        problemDetail.type = URI.create("error")
        problemDetail.setProperty("date", LocalDateTime.now())
        return problemDetail
    }

    private fun buildGroupValidationExceptionsErrors(groupException: GroupValidationException): List<Map<String, Any>> {
        return groupException.exceptions.map { e ->
            buildBaseClientExceptionProblemDetailProperties(e).toMutableMap().apply {
                this["detail"] = e.message ?: "No details available"
            }
        }
    }

    private fun buildBaseClientExceptionProblemDetailProperties(baseClientException: BaseClientException): Map<String, Any> {
        return mapOf(
            "api_error_code" to baseClientException.exceptionName.apiErrorCode,
            "api_error_name" to baseClientException.exceptionName.name,
            "properties" to baseClientException.properties()
        )
    }

    @ExceptionHandler(BaseClientException::class)
    fun handleBaseClientException(baseClientException: BaseClientException): ErrorResponseException {
        val problemDetail = buildBaseClientExceptionProblemDetail(baseClientException)
        return ErrorResponseException(
            baseClientException.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR,
            problemDetail,
            baseClientException
        )
    }
}
