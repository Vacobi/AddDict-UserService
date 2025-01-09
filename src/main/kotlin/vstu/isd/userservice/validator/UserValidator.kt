package vstu.isd.userservice.validator

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import vstu.isd.userservice.config.properties.UserValidationRuleProperties
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.exception.ClientExceptionName
import vstu.isd.userservice.exception.GroupValidationException
import vstu.isd.userservice.exception.ValidationException
import java.util.*

@Component
@EnableConfigurationProperties(UserValidationRuleProperties::class)
class UserValidator(
    private val userValidationRule: UserValidationRuleProperties
) {

    fun validateCreateUserRequest(createUserRequest: CreateUserRequestDto): Optional<GroupValidationException> {
        val exceptions = mutableListOf<ValidationException>()

        validateLogin(createUserRequest.login).ifPresent { exceptions.add(it) }
        validatePassword(createUserRequest.password).ifPresent { exceptions.add(it) }

        return if (exceptions.isEmpty()) {
            Optional.empty()
        } else Optional.of(
            GroupValidationException(exceptions)
        )
    }

    fun validateLogin(login: String): Optional<ValidationException> =
        if (login.matches(Regex(userValidationRule.login.regex))) {
            Optional.empty()
        } else {
            Optional.of(
                ValidationException(
                    "Login must match rules: ${userValidationRule.login.regex}",
                    ClientExceptionName.INVALID_LOGIN
                )
            )
        }

    fun validatePassword(password: String): Optional<ValidationException> =
        if (password.matches(Regex(userValidationRule.password.regex))) {
            Optional.empty()
        } else {
            Optional.of(
                ValidationException(
                    "Password must match rules: ${userValidationRule.password.regex}",
                    ClientExceptionName.INVALID_PASSWORD
                )
            )
        }
}