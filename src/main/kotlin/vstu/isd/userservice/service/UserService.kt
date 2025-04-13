package vstu.isd.userservice.service

import jakarta.transaction.Transactional
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.dto.FindUserRequestDto
import vstu.isd.userservice.dto.UserDto
import vstu.isd.userservice.entity.User
import vstu.isd.userservice.exception.LoginIsNotUniqueException
import vstu.isd.userservice.exception.UserNotFoundException
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
            val loginAlreadyExists = (e.cause as? ConstraintViolationException)
                ?.constraintName == "user_login_key"
            if (loginAlreadyExists) {
                throw LoginIsNotUniqueException(createUserRequest.login)
            }
            throw e
        }.toDto()
    }

    fun findUser(findUserRequest: FindUserRequestDto): User {

        userValidator.validateFindUserRequest(findUserRequest).ifPresent { throw it }

        val probe = findUserRequest.toEntity()
        val matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withIgnorePaths("createdAt", "credentialsUpdatedAt");
        val example = Example.of(probe, matcher)

        return userRepository.findOne(example).orElseThrow{ UserNotFoundException(findUserRequest) }
    }
}