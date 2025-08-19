@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
package kotlinx.datetime.internal

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.datetime.Instant
import platform.framework.OH_TimeService_GetTimeZone
import platform.framework.TIMESERVICE_ERR_INVALID_PARAMETER
import platform.framework.TIMESERVICE_ERR_OK
import platform.posix.CLOCK_REALTIME
import platform.posix.clock_gettime
import platform.posix.timespec
import platform.resource.OH_ResourceManager_CloseRawDir
import platform.resource.OH_ResourceManager_CloseRawFile
import platform.resource.OH_ResourceManager_GetRawFileCount
import platform.resource.OH_ResourceManager_GetRawFileName
import platform.resource.OH_ResourceManager_GetRawFileSize
import platform.resource.OH_ResourceManager_IsRawDir
import platform.resource.OH_ResourceManager_OpenRawDir
import platform.resource.OH_ResourceManager_OpenRawFile
import platform.resource.OH_ResourceManager_ReadRawFile
import kotlin.experimental.ExperimentalNativeApi

internal actual val systemTzdb: TimeZoneDatabase
    get() = object: TimeZoneDatabase {

        override fun rulesForId(id: String): TimeZoneRules {
            val filePath = "tzdb/$id"
            val file = OH_ResourceManager_OpenRawFile(resmgr, filePath) ?: error("$filePath not found")
            val size = OH_ResourceManager_GetRawFileSize(file)
            val array = memScoped {

                val buffer = ByteArray(size.toInt())
                buffer.usePinned {
                    OH_ResourceManager_ReadRawFile(file, it.addressOf(0), size.toULong())
                }
                OH_ResourceManager_CloseRawFile(file)

                buffer
            }
            return readTzFile(array).toTimeZoneRules()
        }

        override fun availableTimeZoneIds(): Set<String> {
            val tzdbDir = OH_ResourceManager_OpenRawDir(resmgr, "tzdb")
            val ids = openDir("", tzdbDir)
            OH_ResourceManager_CloseRawDir(tzdbDir)
            return ids
        }

    }

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

internal actual fun currentTime(): Instant {
    val (seconds, nanos) = memScoped {
        val ts = alloc<timespec>()
        clock_gettime(CLOCK_REALTIME, ts.ptr)
        ts.tv_sec to ts.tv_nsec
    }
    return Instant.fromEpochSeconds(seconds, nanos)
}


private typealias NativeResourceManager = CPointer<cnames.structs.NativeResourceManager>?
private typealias RawDir = CPointer<cnames.structs.RawDir>?
private typealias RawFile = CPointer<cnames.structs.RawFile>?

private var resmgr: NativeResourceManager? = null

private fun openDir(path: String, dir: RawDir): Set<String> = buildSet {
    val count = OH_ResourceManager_GetRawFileCount(dir)
    repeat(count) { index ->
        val file = OH_ResourceManager_GetRawFileName(dir, index)?.toKString() ?: return@repeat
        val filePath = "$path/$file".removePrefix("/")
        val isDir = OH_ResourceManager_IsRawDir(resmgr, "tzdb/$filePath")

        if (isDir) {
            val nextDir = OH_ResourceManager_OpenRawDir(resmgr, "tzdb/$filePath")
            addAll(openDir(filePath, nextDir))
            OH_ResourceManager_CloseRawDir(nextDir)
        } else {
            add(filePath)
        }
    }
}

fun initNativeResourceManagerForDatetime(mgr: NativeResourceManager) {
    resmgr = mgr
}