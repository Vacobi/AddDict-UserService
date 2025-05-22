package vstu.isd.userservice.mapper

import vstu.isd.userservice.dto.SubscribeDto
import vstu.isd.userservice.dto.SubscribeUserRequestDto
import vstu.isd.userservice.entity.Subscribe

fun SubscribeUserRequestDto.toEntity() = Subscribe(null, subscriberId, authorId)

fun Subscribe.toDto() = SubscribeDto(id, subscriber!!, author!!)