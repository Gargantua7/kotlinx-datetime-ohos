@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
package com.sample

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.datetime.internal.initNativeResourceManagerForDatetime
import platform.ohos.napi_env
import platform.ohos.napi_value
import platform.resource.OH_ResourceManager_InitNativeResourceManager
import kotlin.experimental.ExperimentalNativeApi

@CName("initNativeResourceManager")
fun initNativeResourceManager(env: napi_env, jsResMgr: napi_value) {
    initNativeResourceManagerForDatetime(OH_ResourceManager_InitNativeResourceManager(env, jsResMgr))
}