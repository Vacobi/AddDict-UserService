package vstu.isd.userservice.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDate(): Date = Date.from(this@toDate.atZone(ZoneId.systemDefault()).toInstant())