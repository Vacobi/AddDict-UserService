package vstu.isd.userservice.service

import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import vstu.isd.userservice.config.properties.JwtProperties
import vstu.isd.userservice.dto.*
import vstu.isd.userservice.entity.TokenStatus
import vstu.isd.userservice.entity.Token
import vstu.isd.userservice.entity.isExpired
import vstu.isd.userservice.entity.isValid
import vstu.isd.userservice.exception.RefreshTokenIsExpiredException
import vstu.isd.userservice.exception.RefreshTokenIsInvalidException
import vstu.isd.userservice.exception.RefreshTokenNonExistsException
import vstu.isd.userservice.mapper.toUserCredentials
import vstu.isd.userservice.repository.TokenRepository
import vstu.isd.userservice.repository.UserRepository
import vstu.isd.userservice.util.toDate
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userCredentialsService: UserCredentialsService,
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val jwtProperties: JwtProperties,
    private val tokenRepository: TokenRepository
) {
    @Transactional
    fun authenticate(authRequest: AuthRequestDto): AuthResponseDto {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.login,
                authRequest.password
            )
        )

        val userCredentials: UserCredentials = userCredentialsService.loadUserByUsername(authRequest.login)

        val refreshToken = getRefreshToken(userCredentials)
        val accessToken = getAccessToken(userCredentials)

        return AuthResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun getAccessToken(userDetails: UserCredentials): String {
        return tokenService.generate(
            userDetails = userDetails,
            expiredAt = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration.toMillis()),
        )
    }

    private fun getRefreshToken(userDetails: UserCredentials): String {

        var refreshTokenEntity: Token = tokenRepository.findByUserId(userDetails.userId).orElseGet {
            generateRefreshToken(userDetails)
        }

        val isNotValidRefreshToken = !(
                refreshTokenEntity.isValid() &&
                        tokenService.isValidRefreshToken(refreshTokenEntity.refreshToken!!, userDetails)
                )

        if (isNotValidRefreshToken) {
            refreshTokenEntity = generateRefreshToken(userDetails, refreshTokenEntity.id)
        }

        return refreshTokenEntity.refreshToken!!
    }

    private fun generateRefreshToken(userDetails: UserCredentials, tokenId: Long? = null): Token {

        val expireAt = LocalDateTime.now().plus(jwtProperties.refreshTokenExpiration)

        val user = userRepository.getReferenceById(userDetails.userId)

        val refreshTokenEntity = tokenRepository.save(
            Token(
                id = tokenId,
                refreshToken = null,
                expireAt = expireAt,
                status = TokenStatus.ACTIVE,
                user = user,
            )
        )

        val refreshToken = tokenService.generate(
            userDetails = userDetails,
            expiredAt = expireAt.toDate(),
            jti = refreshTokenEntity.id!!.toString(),
        )

        refreshTokenEntity.refreshToken = refreshToken

        return refreshTokenEntity
    }

    @Transactional
    fun refreshAccessToken(refreshRequest: RefreshAccessTokenRequestDto): RefreshAccessTokenRequestDto {

        val refreshToken = tokenRepository.findByRefreshToken(refreshRequest.refreshedToken).orElseThrow {
            throw RefreshTokenNonExistsException(refreshRequest.refreshedToken)
        }

        if (refreshToken.isExpired()) {
            throw RefreshTokenIsExpiredException(refreshRequest.refreshedToken)
        }

        val actualUserCredentials = refreshToken.user!!.toUserCredentials()

        val isValidToken = tokenService.isValidRefreshToken(refreshRequest.refreshedToken, actualUserCredentials)

        return if (isValidToken) {
            val accessToken = getAccessToken(actualUserCredentials)
            RefreshAccessTokenRequestDto(accessToken)
        } else {
            throw RefreshTokenIsInvalidException(refreshRequest.refreshedToken)
        }
    }

    fun verifyAccessToken(token: VerifyAccessTokenRequestDto): Boolean =
        tokenService.isValidToken(token.accessToken)

}