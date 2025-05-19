package vstu.isd.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import vstu.isd.userservice.entity.Subscribe
import java.util.*

public interface SubscribeRepository : JpaRepository<Subscribe, Long>, QueryByExampleExecutor<Subscribe> {
    fun findBySubscriber(subscriber: Long): Optional<Subscribe>
    fun findByAuthor(author: Long): Optional<Subscribe>
}
