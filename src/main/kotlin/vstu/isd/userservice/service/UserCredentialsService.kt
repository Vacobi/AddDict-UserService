package vstu.isd.userservice.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import vstu.isd.userservice.dto.UserCredentials
import vstu.isd.userservice.mapper.toUserCredentials
import vstu.isd.userservice.repository.UserRepository

@Service
class UserCredentialsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserCredentials = userRepository.findByLogin(username)
        .orElseThrow { UsernameNotFoundException("User with login $username not found") }
        .toUserCredentials()
}