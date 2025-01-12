package vstu.isd.userservice.testutils

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import vstu.isd.userservice.dto.UserDto
import vstu.isd.userservice.entity.User
import java.time.Duration
import java.time.LocalDateTime

class TestAsserts {

    companion object {

        fun assertUserDtoEquals(expected: UserDto, actual: UserDto) {

            assertEquals(expected.id, actual.id)
            assertEquals(expected.login, actual.login)
        }

        fun assertUserEquals(expected: User, actual: User) {

            assertEquals(expected.id, actual.id)
            assertEquals(expected.login, actual.login)
            assertEquals(expected.password, actual.password)
            assertLocalDateTimeEquals(expected.createdAt, actual.createdAt)
            assertLocalDateTimeEquals(expected.credentialsUpdatedAt, actual.credentialsUpdatedAt)
        }

        private fun assertLocalDateTimeEquals(expected: LocalDateTime?, actual: LocalDateTime?) {
            if (expected != null && actual != null) {
                assertTrue(Duration.between(expected, actual).seconds < 1)
            } else if (expected == null && actual == null) {
                assertEquals(expected, actual)
            } else {
                fail()
            }
        }

    }
}