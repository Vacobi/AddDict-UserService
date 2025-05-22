package vstu.isd.userservice.validator

import org.springframework.stereotype.Component
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.exception.ClientExceptionName
import vstu.isd.userservice.exception.GroupValidationException
import vstu.isd.userservice.exception.ValidationException
import java.util.Optional

@Component
class SubscribeValidator {

    fun validateSubscribeUserRequestDto(subscribeRequestDto: SubscribeUserRequestDto): Optional<GroupValidationException> {
        val exceptions = mutableListOf<ValidationException>()

        if (subscribeRequestDto.subscriberId < 0) {
            exceptions.add(ValidationException(
                "",
                ClientExceptionName.INVALID_SUBSCRIBE_USER_REQUEST)
            )
        }

        if (subscribeRequestDto.authorId < 0) {
            exceptions.add(ValidationException(
                "",
                ClientExceptionName.INVALID_SUBSCRIBE_USER_REQUEST)
            )
        }

        return if (exceptions.isEmpty()) {
            Optional.empty()
        } else Optional.of(
            GroupValidationException(exceptions)
        )
    }
}