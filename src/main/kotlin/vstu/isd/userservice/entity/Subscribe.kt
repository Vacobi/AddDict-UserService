package vstu.isd.userservice.entity

import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "subscribe")
class Subscribe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber")
    var subscriber: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    var author: User? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(other) != Hibernate.getClass(this)) return false
        other as Subscribe
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}

fun Subscribe.compare(t: Subscribe): Boolean =
    this.id == t.id &&
            this.subscriber == t.subscriber &&
            this.author == t.author
