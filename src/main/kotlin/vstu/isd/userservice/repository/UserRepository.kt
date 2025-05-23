package vstu.isd.userservice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query
import vstu.isd.userservice.entity.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): Optional<User>

    @Query(
        """
        SELECT u.* 
        FROM "user" u
        INNER JOIN subscribe s ON u.id = s.subscriber
        WHERE s.author = :authorId
        """,
        nativeQuery = true
    )
    fun findSubscribersByAuthorId(authorId: Long): List<User>
}