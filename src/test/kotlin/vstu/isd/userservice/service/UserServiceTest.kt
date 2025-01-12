package vstu.isd.userservice.service

import lombok.extern.slf4j.Slf4j
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ContextConfiguration
import vstu.isd.userservice.config.TestContainersConfig
import vstu.isd.userservice.dto.CreateUserRequestDto
import vstu.isd.userservice.entity.User
import vstu.isd.userservice.exception.ClientExceptionName
import vstu.isd.userservice.exception.GroupValidationException
import vstu.isd.userservice.exception.LoginIsNotUniqueException
import vstu.isd.userservice.exception.ValidationException
import vstu.isd.userservice.repository.UserRepository
import vstu.isd.userservice.testutils.TestAsserts.Companion.assertUserDtoEquals
import vstu.isd.userservice.testutils.TestAsserts.Companion.assertUserEquals
import java.time.LocalDateTime

@SpringBootTest
@ContextConfiguration(initializers = [TestContainersConfig::class])
@Slf4j
class UserServiceTest {

    @SpyBean
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userService: UserService

    companion object {
        private var login = 10
        private var password = Integer.MAX_VALUE

        fun getNextLogin(): String {
            return login++.toString()
        }

        fun getNextPassword(): String {
            return password--.toString()
        }
    }

  @org.junit.jupiter.api.Nested
  inner class CreateUserTest{

   @Test
   fun `create user`(){

       val createUserRequestDto = CreateUserRequestDto(
           getNextLogin(),
           getNextPassword()
       )

       val countOfUsersBeforeCreate = userRepository.count()
       val actualCreatedUser = userService.createUser(createUserRequestDto)
       val countOfUsersAfterCreate = userRepository.count()

       val expectedCreatedUser = actualCreatedUser.copy(login = createUserRequestDto.login)
       assertUserDtoEquals(expectedCreatedUser, actualCreatedUser)
       assertEquals(countOfUsersBeforeCreate + 1, countOfUsersAfterCreate)
       val actualCreatedUserInRepos = userRepository.findByLogin(expectedCreatedUser.login).get()
       assertNotEquals(createUserRequestDto.password, actualCreatedUserInRepos.password)
       val expectedCreatedUserInRepos = User(
           id = actualCreatedUser.id,
           login = createUserRequestDto.login,
           password = actualCreatedUserInRepos.password,
           createdAt = LocalDateTime.now(),
           credentialsUpdatedAt = LocalDateTime.now()
       )
       assertUserEquals(expectedCreatedUserInRepos, actualCreatedUserInRepos)
   }

      @Test
      fun `create two users`(){

          val createFirstUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              getNextPassword()
          )
          val createSecondUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              getNextPassword()
          )


          val countOfUsersBeforeCreate = userRepository.count()
          val actualCreatedFirstUser = userService.createUser(createFirstUserRequestDto)
          val actualCreatedSecondUser = userService.createUser(createSecondUserRequestDto)
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate + 2, countOfUsersAfterCreate)

          val expectedCreatedFirstUser = actualCreatedFirstUser.copy(login = createFirstUserRequestDto.login)
          assertUserDtoEquals(expectedCreatedFirstUser, actualCreatedFirstUser)
          val actualCreatedFirstUserInRepos = userRepository.findByLogin(expectedCreatedFirstUser.login).get()
          val expectedCreatedFirstUserInRepos = User(
              id = actualCreatedFirstUser.id,
              login = createFirstUserRequestDto.login,
              password = actualCreatedFirstUserInRepos.password,
              createdAt = LocalDateTime.now(),
              credentialsUpdatedAt = LocalDateTime.now()
          )
          assertUserEquals(expectedCreatedFirstUserInRepos, actualCreatedFirstUserInRepos)

          val expectedCreatedSecondUser = actualCreatedSecondUser.copy(login = createSecondUserRequestDto.login)
          assertUserDtoEquals(expectedCreatedSecondUser, actualCreatedSecondUser)
          val actualCreatedSecondUserInRepos = userRepository.findByLogin(expectedCreatedSecondUser.login).get()
          val expectedCreatedSecondUserInRepos = User(
              id = actualCreatedSecondUser.id,
              login = createSecondUserRequestDto.login,
              password = actualCreatedSecondUserInRepos.password,
              createdAt = LocalDateTime.now(),
              credentialsUpdatedAt = LocalDateTime.now()
          )
          assertUserEquals(expectedCreatedSecondUserInRepos, actualCreatedSecondUserInRepos)
      }

      @Test
      fun `create users with same logins`(){

          val createFirstUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              getNextPassword()
          )
          val createSecondUserRequestDto = CreateUserRequestDto(
              createFirstUserRequestDto.login,
              getNextPassword()
          )


          val countOfUsersBeforeCreate = userRepository.count()
          userService.createUser(createFirstUserRequestDto)
          val actualCreatedSecondUserException = assertThrows<LoginIsNotUniqueException> {
              userService.createUser(createSecondUserRequestDto)
          }
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate + 1, countOfUsersAfterCreate)

          val expectedExceptionName = ClientExceptionName.LOGIN_IS_NOT_UNIQUE
          val actualExceptionName = actualCreatedSecondUserException.exceptionName
          assertEquals(expectedExceptionName, actualExceptionName)
      }

      @Test
      fun `create users with same passwords`(){

          val createFirstUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              getNextPassword()
          )
          val createSecondUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              createFirstUserRequestDto.password
          )


          val countOfUsersBeforeCreate = userRepository.count()
          userService.createUser(createFirstUserRequestDto)
          assertDoesNotThrow {
              userService.createUser(createSecondUserRequestDto)
          }
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate + 2, countOfUsersAfterCreate)
      }

      @Test
      fun `invalid login`(){

          val createUserRequestDto = CreateUserRequestDto(
              "My!Login",
              getNextPassword()
          )


          val countOfUsersBeforeCreate = userRepository.count()
          val actualCreatedUserGroupException = assertThrows<GroupValidationException> {
              userService.createUser(createUserRequestDto)
          }
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate, countOfUsersAfterCreate)

          val expectedExceptionsNames = listOf(
              ClientExceptionName.INVALID_LOGIN
          )
          val actualExceptionsNames = actualCreatedUserGroupException.exceptions.stream()
              .map(ValidationException::exceptionName)
              .toList()
          assertEquals(expectedExceptionsNames, actualExceptionsNames)
      }

      @Test
      fun `invalid password`(){

          val createUserRequestDto = CreateUserRequestDto(
              getNextLogin(),
              ".,.,.,.,."
          )


          val countOfUsersBeforeCreate = userRepository.count()
          val actualCreatedUserGroupException = assertThrows<GroupValidationException> {
              userService.createUser(createUserRequestDto)
          }
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate, countOfUsersAfterCreate)

          val expectedExceptionsNames = listOf(
              ClientExceptionName.INVALID_PASSWORD
          )
          val actualExceptionsNames = actualCreatedUserGroupException.exceptions.stream()
              .map(ValidationException::exceptionName)
              .toList()
          assertEquals(expectedExceptionsNames, actualExceptionsNames)
      }

      @Test
      fun `invalid login and password`(){

          val createUserRequestDto = CreateUserRequestDto(
              "My!Login",
              ".,.,.,.,."
          )


          val countOfUsersBeforeCreate = userRepository.count()
          val actualCreatedUserGroupException = assertThrows<GroupValidationException> {
              userService.createUser(createUserRequestDto)
          }
          val countOfUsersAfterCreate = userRepository.count()


          assertEquals(countOfUsersBeforeCreate, countOfUsersAfterCreate)

          val expectedExceptionsNames = listOf(
              ClientExceptionName.INVALID_LOGIN,
              ClientExceptionName.INVALID_PASSWORD
          )
          val actualExceptionsNames = actualCreatedUserGroupException.exceptions.stream()
              .map(ValidationException::exceptionName)
              .toList()
          assertEquals(expectedExceptionsNames, actualExceptionsNames)
      }
  }
}