#include "libkn_api.h"
#include "napi/native_api.h"
#include "rawfile/raw_file_manager.h"
#include <rawfile/raw_dir.h>

static napi_value Add(napi_env env, napi_callback_info info)
{
    size_t argc = 2;
    napi_value args[2] = {nullptr};

    napi_get_cb_info(env, info, &argc, args , nullptr, nullptr);

    napi_valuetype valuetype0;
    napi_typeof(env, args[0], &valuetype0);

    napi_valuetype valuetype1;
    napi_typeof(env, args[1], &valuetype1);

    double value0;
    napi_get_value_double(env, args[0], &value0);

    double value1;
    napi_get_value_double(env, args[1], &value1);

    napi_value sum;
    napi_create_double(env, value0 + value1, &sum);

    return sum;

}

static napi_value InitNativeResourceManager(napi_env env, napi_callback_info info)
{
    size_t argc = 1;
    napi_value jsResMgr;
    napi_get_cb_info(env, info, &argc, &jsResMgr, nullptr, nullptr);
    initNativeResourceManager(env, jsResMgr);
    return nullptr;
}

static napi_value GetCurrentDateTime(napi_env env, napi_callback_info info) 
{
    const char* str = getCurrentDateTime();
    napi_value result;
    napi_create_string_utf8(env, str, NAPI_AUTO_LENGTH, &result);
    return result;
}

static napi_value GetCurrentTimeZoneId(napi_env env, napi_callback_info info) 
{
    const char* str = getCurrentTimeZoneId();
    napi_value result;
    napi_create_string_utf8(env, str, NAPI_AUTO_LENGTH, &result);
    return result;
}

static napi_value GetCurrentTimestamp(napi_env env, napi_callback_info info) 
{
    long long timestamp = getCurrentTimestamp();
    napi_value result;
    napi_create_int64(env, timestamp, &result);
    return result;
    
}

static napi_value GetTimeZoneOffset(napi_env env, napi_callback_info info) 
{
    int timestamp = getTimeZoneOffset();
    napi_value result;
    napi_create_int32(env, timestamp, &result);
    return result;
}

static napi_value GetAvailableTimeZoneIDs(napi_env env, napi_callback_info info) 
{
    const char* str = getAvailableTimeZoneIDs();
    napi_value result;
    napi_create_string_utf8(env, str, NAPI_AUTO_LENGTH, &result);
    return result;
}

EXTERN_C_START
static napi_value Init(napi_env env, napi_value exports)
{
    napi_property_descriptor desc[] = {
        { "add", nullptr, Add, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "initNativeResourceManager", nullptr, InitNativeResourceManager, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "getCurrentDateTime", nullptr, GetCurrentDateTime, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "getCurrentTimeZoneId", nullptr, GetCurrentTimeZoneId, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "getCurrentTimestamp", nullptr, GetCurrentTimestamp, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "getTimeZoneOffset", nullptr, GetTimeZoneOffset, nullptr, nullptr, nullptr, napi_default, nullptr },
        { "getAvailableTimeZoneIDs", nullptr, GetAvailableTimeZoneIDs, nullptr, nullptr, nullptr, napi_default, nullptr },
    };
    napi_define_properties(env, exports, sizeof(desc) / sizeof(desc[0]), desc);
    return exports;
}
EXTERN_C_END

static napi_module demoModule = {
    .nm_version = 1,
    .nm_flags = 0,
    .nm_filename = nullptr,
    .nm_register_func = Init,
    .nm_modname = "entry",
    .nm_priv = ((void*)0),
    .reserved = { 0 },
};

extern "C" __attribute__((constructor)) void RegisterEntryModule(void)
{
    napi_module_register(&demoModule);
}
