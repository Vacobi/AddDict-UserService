package vstu.isd.userservice.controller

import org.springframework.web.bind.annotation.*
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.service.SubscribeService

@RestController
@RequestMapping("api/v1/subscribe")
class SubscribeController (
    private val subscribeService: SubscribeService,
){

    @PostMapping
    fun subscribe(
        @RequestBody subscribeDto: SubscribeUserRequestDto
    ) = subscribeService.subscribe(subscribeDto)
}
