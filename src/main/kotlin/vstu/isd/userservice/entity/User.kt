package vstu.isd.userservice.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "\"user\"")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 32)
    var login: String? = null,

    @Column(nullable = false, length = 64)
    var password: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "credentials_updated_at", nullable = false)
    var credentialsUpdatedAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(other) != Hibernate.getClass(this)) return false
        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}

fun User.compare(u: User): Boolean {
    return this.id == u.id && this.login == u.login && this.password == u.password
}

