package vstu.isd.userservice.service

import jakarta.transaction.Transactional
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.dto.UserDto
import vstu.isd.userservice.entity.User
import vstu.isd.userservice.mapper.toDto
import vstu.isd.userservice.mapper.toEntity
import vstu.isd.userservice.repository.UserRepository
import vstu.isd.userservice.validator.UserValidator

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val userValidator: UserValidator
) {
    @Transactional
    fun createUser(createUserRequest: CreateUserRequestDto): UserDto {

        userValidator.validateCreateUserRequest(createUserRequest).ifPresent { throw it }

        val user: User = createUserRequest.toEntity().apply {
            password = encoder.encode(password)
        }

        return try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            if (
                (e.cause as? ConstraintViolationException)?.constraintName == "user_login_key"
            ) {
                throw IllegalArgumentException("User with login ${createUserRequest.login} already exists")
            }
            throw e
        }.toDto()
    }
}