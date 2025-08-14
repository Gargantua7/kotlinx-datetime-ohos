package kotlinx.datetime.internal

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.datetime.Instant
import kotlinx.datetime.UtcOffset
import platform.framework.OH_TimeService_GetTimeZone
import platform.framework.TIMESERVICE_ERR_INVALID_PARAMETER
import platform.framework.TIMESERVICE_ERR_OK
import platform.posix.time

internal actual val systemTzdb: TimeZoneDatabase
    get() = object: TimeZoneDatabase {

        override fun rulesForId(id: String): TimeZoneRules = supportTimezone[id]?.let { offset ->
            TimeZoneRules(emptyList(), listOf(UtcOffset(offset)), null)
        } ?: throw IllegalArgumentException("Unknown timezone $id")

        override fun availableTimeZoneIds(): Set<String> = supportTimezone.keys

    }

@OptIn(ExperimentalForeignApi::class)
internal actual fun currentSystemDefaultZone(): Pair<String, TimeZoneRules?> {
    val zone = memScoped {

        var size = 16u

        do {
            size *= 2u

            val buffer = allocArray<ByteVar>(size.toInt())
            val code = OH_TimeService_GetTimeZone(buffer, size)

            if (code == TIMESERVICE_ERR_OK) {
                return@memScoped buffer.toKString()
            }

        } while (code == TIMESERVICE_ERR_INVALID_PARAMETER && size <= 512u)

        error("TIMESERVICE_ERR_INTERNAL_ERROR")
    }

    return zone to systemTzdb.rulesForId(zone)
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun currentTime(): Instant {
    return Instant.fromEpochSeconds(time(null))
}

// see https://developer.huawei.com/consumer/cn/doc/harmonyos-references/js-apis-date-time#%E6%94%AF%E6%8C%81%E7%9A%84%E7%B3%BB%E7%BB%9F%E6%97%B6%E5%8C%BA
private val supportTimezone = mapOf(
    "Antarctica/McMurdo" to 12,
    "America/Argentina/Buenos_Aires" to -3,
    "Australia/Sydney" to 10,
    "America/Noronha" to -2,
    "America/St_Johns" to -3,
    "Africa/Kinshasa" to 1,
    "America/Santiago" to -3,
    "Asia/Shanghai" to 8,
    "Asia/Nicosia" to 3,
    "Europe/Berlin" to 2,
    "America/Guayaquil" to -5,
    "Europe/Madrid" to 2,
    "Pacific/Pohnpei" to 11,
    "America/Godthab" to -2,
    "Asia/Jakarta" to 7,
    "Pacific/Tarawa" to 12,
    "Asia/Almaty" to 6,
    "Pacific/Majuro" to 12,
    "Asia/Ulaanbaatar" to 8,
    "America/Mexico_City" to -5,
    "Asia/Kuala_Lumpur" to 8,
    "Pacific/Auckland" to 12,
    "Pacific/Tahiti" to -10,
    "Pacific/Port_Moresby" to 10,
    "Asia/Gaza" to 3,
    "Europe/Lisbon" to 1,
    "Europe/Moscow" to 3,
    "Europe/Kiev" to 3,
    "Pacific/Wake" to 12,
    "America/New_York" to -4,
    "Asia/Tashkent" to 5
)