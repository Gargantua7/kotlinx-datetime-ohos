@file:OptIn(ExperimentalNativeApi::class)
package com.sample

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import kotlin.experimental.ExperimentalNativeApi

@CName("getCurrentTimeZoneId")
fun getCurrentTimeZoneId(): String = runCatching { TimeZone.currentSystemDefault().id }.getOrElse { it.toString() }

@CName("getCurrentTimestamp")
fun getCurrentTimestamp(): Long = Clock.System.now().toEpochMilliseconds()

@CName("getCurrentDateTime")
fun getCurrentDateTime(): String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let {
    "${it.year}.${it.monthNumber}.${it.dayOfMonth} ${it.hour}:${it.minute}:${it.second}:${it.nanosecond}"
}

@CName("getTimeZoneOffset")
fun getTimeZoneOffset(): Int = TimeZone.currentSystemDefault().offsetAt(Clock.System.now()).totalSeconds
