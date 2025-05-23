package vstu.isd.userservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.service.SubscribeService
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RestController
@RequestMapping("api/v1/subscribe")
class SubscribeController (
    private val subscribeService: SubscribeService,
){

    @PostMapping
    fun subscribe(
        @RequestBody subscribeDto: SubscribeUserRequestDto
    ) = subscribeService.subscribe(subscribeDto)

    @DeleteMapping("/unsubscribe")
    fun unsubscribe(
        @RequestParam subscriberId: Long,
        @RequestParam authorId: Long,
    ) : ResponseEntity<Any> {
        subscribeService.unsubscribe(subscriberId, authorId)
        return ResponseEntity.ok().build()
    }
}
