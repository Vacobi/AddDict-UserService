package vstu.isd.userservice.exception

class GroupValidationException(
    val exceptions: List<ValidationException>
) : ValidationException(
    "Group validation failed",
    ClientExceptionName.GROUP_VALIDATION_EXCEPTION
)