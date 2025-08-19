#ifndef GLOBAL_NATIVE_RESOURCE_MANAGER_H
#define GLOBAL_NATIVE_RESOURCE_MANAGER_H

#include "napi/native_api.h"
#include "raw_file.h"
#include "raw_dir.h"

#ifdef __cplusplus
extern "C" {
#endif

struct NativeResourceManager;

typedef struct NativeResourceManager NativeResourceManager;

NativeResourceManager *OH_ResourceManager_InitNativeResourceManager(napi_env env, napi_value jsResMgr);

void OH_ResourceManager_ReleaseNativeResourceManager(NativeResourceManager *resMgr);

RawDir *OH_ResourceManager_OpenRawDir(const NativeResourceManager *mgr, const char *dirName);

RawFile *OH_ResourceManager_OpenRawFile(const NativeResourceManager *mgr, const char *fileName);

bool OH_ResourceManager_IsRawDir(const NativeResourceManager *mgr, const char *path);

#ifdef __cplusplus
};
#endif

/** @} */
#endif // GLOBAL_NATIVE_RESOURCE_MANAGER_H
