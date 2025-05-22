package vstu.isd.userservice.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ContextConfiguration
import vstu.isd.userservice.config.TestContainersConfig
import vstu.isd.userservice.config.properties.UserValidationRuleProperties
import vstu.isd.userservice.dto.SubscribeDto
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.entity.User
import vstu.isd.userservice.exception.SubscribeNotUniqueException
import vstu.isd.userservice.exception.UserNotFoundException
import vstu.isd.userservice.repository.SubscribeRepository
import vstu.isd.userservice.repository.UserRepository
import vstu.isd.userservice.testutils.TestAsserts.Companion.assertSubscribeDtoEquals
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@EnableConfigurationProperties(UserValidationRuleProperties::class)
@ContextConfiguration(initializers = [TestContainersConfig::class])
class SubscribeServiceTest {

 @SpyBean
 private lateinit var subscribeRepository: SubscribeRepository

 @Autowired
 private lateinit var subscribeService: SubscribeService

 companion object {
  private var login = 10
  private var password = AtomicInteger(Int.MAX_VALUE)

  @SpyBean
  private lateinit var userRepository: UserRepository

  fun getNextLogin(): String {
   return login++.toString()
  }

  fun getNextPassword(): String {
   return password.getAndDecrement().toString()
  }

  private fun addUserToRepository(): User {
   val user = UserServiceTest.getNextUser()
   userRepository.save(user)
   return user
  }
 }

 @Nested
 inner class CreateSubscribeTest {

  @Test
  fun createSubscribe() {
   val author: User = addUserToRepository()
   val subscriber: User = addUserToRepository()
   val requestDto = SubscribeUserRequestDto(subscriber.id!!, author.id!!)

   val actualDto: SubscribeDto = subscribeService.subscribe(requestDto)

   val expectedDto = SubscribeDto(actualDto.id!!, subscriber.id!!, author.id!!)

   assertNotNull(actualDto.id)
   assertSubscribeDtoEquals(expectedDto, actualDto)
  }

  @Test
  fun createSubscribeToNonExistingUser() {
   val author: User = addUserToRepository()
   val requestDto = SubscribeUserRequestDto(Long.MAX_VALUE, author.id!!)

   val subscribesCountInRepoBeforeSubscribe = subscribeRepository.count()
   assertThrows<UserNotFoundException> {
    subscribeService.subscribe(requestDto)
   }
   val subscribesCountInRepoAfterSubscribe = subscribeRepository.count()

   assertEquals(subscribesCountInRepoBeforeSubscribe, subscribesCountInRepoAfterSubscribe)
  }

  @Test
  fun createSubscribeByNonExistingUser() {
   val subscriber: User = addUserToRepository()
   val requestDto = SubscribeUserRequestDto(subscriber.id!!, Long.MAX_VALUE)

   val subscribesCountInRepoBeforeSubscribe = subscribeRepository.count()
   assertThrows<UserNotFoundException> {
    subscribeService.subscribe(requestDto)
   }
   val subscribesCountInRepoAfterSubscribe = subscribeRepository.count()

   assertEquals(subscribesCountInRepoBeforeSubscribe, subscribesCountInRepoAfterSubscribe)
  }

  @Test
  fun createTwoSameSubscribes() {
   val author: User = addUserToRepository()
   val subscriber: User = addUserToRepository()
   val requestDto = SubscribeUserRequestDto(subscriber.id!!, author.id!!)

   subscribeService.subscribe(requestDto)
   val subscribesCountInRepoBeforeSubscribe = subscribeRepository.count()
   assertThrows<SubscribeNotUniqueException> {
    subscribeService.subscribe(requestDto)
   }
   val subscribesCountInRepoAfterSubscribe = subscribeRepository.count()

   assertEquals(subscribesCountInRepoBeforeSubscribe, subscribesCountInRepoAfterSubscribe)
  }
 }

 @Nested
 inner class UnsubscribeTest {

  @Test
  fun unsubscribe() {
   val author: User = addUserToRepository()
   val subscriber: User = addUserToRepository()
   val expectedUnsubscribed = true

   val subscribeDto = subscribeService.subscribe(SubscribeUserRequestDto(subscriber.id!!, author.id!!))

   val subscribesCountInRepoBeforeSubscribe = subscribeRepository.count()
   val actualUnsubscribed: Boolean = subscribeService.unsubscribe(subscribeDto.id!!)
   val subscribesCountInRepoAfterSubscribe = subscribeRepository.count()

   assertEquals(expectedUnsubscribed, actualUnsubscribed)
   assertEquals(subscribesCountInRepoBeforeSubscribe - 1, subscribesCountInRepoAfterSubscribe)
  }
 }
}
