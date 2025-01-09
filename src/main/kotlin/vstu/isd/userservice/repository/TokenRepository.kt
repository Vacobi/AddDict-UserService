package vstu.isd.userservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import vstu.isd.userservice.entity.Token
import java.util.Optional

interface TokenRepository : JpaRepository<Token, Long> {
    fun findByUserId(userId: Long): Optional<Token>
    fun findByRefreshToken(refreshToken: String): Optional<Token>
}