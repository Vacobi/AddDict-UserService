package vstu.isd.userservice.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.LocalDateTime

@Entity
@Table(name = "token")
class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "refresh_token", unique = true)
    var refreshToken: String? = null,

    @Column(name = "expire_at")
    var expireAt: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    var status: TokenStatus? = TokenStatus.ACTIVE,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(other) != Hibernate.getClass(this)) return false
        other as Token
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}

enum class TokenStatus {
    ACTIVE,
    REVOKED
}

fun Token.compare(t: Token): Boolean =
    this.id == t.id &&
            this.refreshToken == t.refreshToken &&
            this.status == t.status &&
            this.user!!.id == t.user!!.id

fun Token.isValid(): Boolean = status == TokenStatus.ACTIVE && isNotExpired()

fun Token.isExpired(): Boolean = expireAt!!.isBefore(LocalDateTime.now())


fun Token.isNotExpired(): Boolean = !isExpired()

