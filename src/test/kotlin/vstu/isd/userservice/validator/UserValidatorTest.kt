package vstu.isd.userservice.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import vstu.isd.userservice.config.properties.UserValidationRuleProperties
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.dto.FindUserRequestDto
import vstu.isd.userservice.exception.ClientExceptionName
import vstu.isd.userservice.exception.ValidationException
import kotlin.math.max

@SpringBootTest
@EnableConfigurationProperties(UserValidationRuleProperties::class)
class UserValidatorTest {

    @Autowired
    protected lateinit var userValidationRule: UserValidationRuleProperties

  @Autowired
  private lateinit var userValidator: UserValidator

    private val loginMinLength by lazy { userValidationRule.login.minLength }
    private val loginMaxLength by lazy { userValidationRule.login.maxLength }

    private val passwordMinLength by lazy { userValidationRule.password.minLength }
    private val passwordMaxLength by lazy { userValidationRule.password.maxLength }

    fun generateStringOfLength(length: Int): String {

        return "a".repeat(max(0.0, length.toDouble()).toInt())
    }

    // login <=========================================================================================================8
    @org.junit.jupiter.api.Nested
    inner class ValidateLoginTest {
        @Test
        fun `login contains unresolved symbols`() {

            val login = "It'sMyLogin"

            val actualOptionalException = userValidator.validateLogin(login)

            val expectedExceptionName = ClientExceptionName.INVALID_LOGIN
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }

        @Test
        fun `login contains all resolved lowercase letters`() {

            val login = "abcdefghijklmnopqrstuvwxyz"

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login contains all resolved uppercase letters`() {

            val login = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login contains all digits and underscore`() {

            val login = "0123456789_"

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login starts with underscore`() {

            val login = "_0123456789"

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login contains all types of resolved symbols`() {

            val login = "My_login123"

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login length less than min`() {

            val login = generateStringOfLength(loginMinLength - 1)

            val actualOptionalException = userValidator.validateLogin(login)

            val expectedExceptionName = ClientExceptionName.INVALID_LOGIN
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }

        @Test
        fun `login length is min`() {

            val login = generateStringOfLength(loginMinLength)

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login length is max`() {

            val login = generateStringOfLength(loginMaxLength)

            val actualOptionalException = userValidator.validateLogin(login)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `login length higher than max`() {

            val login = generateStringOfLength(loginMaxLength + 1)

            val actualOptionalException = userValidator.validateLogin(login)

            val expectedExceptionName = ClientExceptionName.INVALID_LOGIN
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }
    }

    // password <======================================================================================================8
    @org.junit.jupiter.api.Nested
    inner class ValidatePasswordTest {
        @Test
        fun `password contains unresolved symbols`() {

            val password = "It'sMyLogin"

            val actualOptionalException = userValidator.validatePassword(password)

            val expectedExceptionName = ClientExceptionName.INVALID_PASSWORD
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }

        @Test
        fun `password contains all resolved lowercase letters`() {

            val password = "abcdefghijklmnopqrstuvwxyz"

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password contains all resolved uppercase letters`() {

            val password = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password contains all digits and three resolved symbols`() {

            val password = "0123456789_!#"

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password starts with underscore`() {

            val password = "_0123456789"

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password contains all types of resolved symbols`() {

            val password = "My_login!123"

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password length less than min`() {

            val password = generateStringOfLength(passwordMinLength - 1)

            val actualOptionalException = userValidator.validatePassword(password)

            val expectedExceptionName = ClientExceptionName.INVALID_PASSWORD
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }

        @Test
        fun `password length is min`() {

            val password = generateStringOfLength(passwordMinLength)

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password length is max`() {

            val password = generateStringOfLength(passwordMaxLength)

            val actualOptionalException = userValidator.validatePassword(password)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `password length higher than max`() {

            val password = generateStringOfLength(passwordMaxLength + 1)

            val actualOptionalException = userValidator.validatePassword(password)

            val expectedExceptionName = ClientExceptionName.INVALID_PASSWORD
            assertThat(actualOptionalException.isPresent)
            assertEquals(expectedExceptionName, actualOptionalException.get().exceptionName)
        }
    }

    @org.junit.jupiter.api.Nested
    inner class validateCreateUserRequestTest {

        @Test
        fun invalidLogin() {

            val createUserRequest = CreateUserRequestDto(
                login = "My!Login",
                password = "myPassword"
            )

            val actualOptionalException = userValidator.validateCreateUserRequest(createUserRequest)

            assertThat(actualOptionalException.isPresent)
            val expectedExceptionsNames = listOf(
                ClientExceptionName.INVALID_LOGIN
            )
            val actualExceptionsNames = actualOptionalException.get().exceptions.stream()
                .map(ValidationException::exceptionName)
                .toList()
            assertEquals(expectedExceptionsNames, actualExceptionsNames)
        }

        @Test
        fun invalidPassword() {

            val createUserRequest = CreateUserRequestDto(
                login = "MyLogin",
                password = "my<=Password"
            )

            val actualOptionalException = userValidator.validateCreateUserRequest(createUserRequest)

            assertThat(actualOptionalException.isPresent)
            val expectedExceptionsNames = listOf(
                ClientExceptionName.INVALID_PASSWORD
            )
            val actualExceptionsNames = actualOptionalException.get().exceptions.stream()
                .map(ValidationException::exceptionName)
                .toList()
            assertEquals(expectedExceptionsNames, actualExceptionsNames)
        }

        @Test
        fun invalidLoginAndPassword() {

            val createUserRequest = CreateUserRequestDto(
                login = "My!Login",
                password = "my<=Password"
            )

            val actualOptionalException = userValidator.validateCreateUserRequest(createUserRequest)

            assertThat(actualOptionalException.isPresent)
            val expectedExceptionsNames = listOf(
                ClientExceptionName.INVALID_LOGIN,
                ClientExceptionName.INVALID_PASSWORD
            )
            val actualExceptionsNames = actualOptionalException.get().exceptions.stream()
                .map(ValidationException::exceptionName)
                .toList()
            assertEquals(expectedExceptionsNames, actualExceptionsNames)
        }

        @Test
        fun allIsValid() {

            val createUserRequest = CreateUserRequestDto(
                login = "MyLogin",
                password = "my123Password"
            )

            val actualOptionalException = userValidator.validateCreateUserRequest(createUserRequest)

            assertThat(actualOptionalException.isEmpty)
        }
    }

    @org.junit.jupiter.api.Nested
    inner class ValidateFindUserRequestTest {
        @Test
        fun `valid find user request`() {
            val findUserRequest = FindUserRequestDto(null, "abcd123")

            val actualOptionalException = userValidator.validateFindUserRequest(findUserRequest)

            assertThat(actualOptionalException.isEmpty)
        }

        @Test
        fun `invalid login`() {
            val findUserRequest = FindUserRequestDto(null, "a")

            val actualOptionalException = userValidator.validateFindUserRequest(findUserRequest)

            assertThat(actualOptionalException.isPresent)
            val expectedExceptionsNames = listOf(
                ClientExceptionName.INVALID_LOGIN
            )
            val actualExceptionsNames = actualOptionalException.get().exceptions.stream()
                .map(ValidationException::exceptionName)
                .toList()
            assertEquals(expectedExceptionsNames, actualExceptionsNames)
        }

        @Test
        fun `empty request`() {
            val findUserRequest = FindUserRequestDto(null, null)

            val actualOptionalException = userValidator.validateFindUserRequest(findUserRequest)

            assertThat(actualOptionalException.isPresent)
            val expectedExceptionsNames = listOf(
                ClientExceptionName.INVALID_FIND_USER_REQUEST
            )
            val actualExceptionsNames = actualOptionalException.get().exceptions.stream()
                .map(ValidationException::exceptionName)
                .toList()
            assertEquals(expectedExceptionsNames, actualExceptionsNames)
        }
    }
 }