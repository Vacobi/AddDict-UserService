package vstu.isd.userservice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository;
import vstu.isd.userservice.entity.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): Optional<User>
}