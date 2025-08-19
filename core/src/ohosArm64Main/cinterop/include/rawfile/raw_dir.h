#ifndef GLOBAL_RAW_DIR_H
#define GLOBAL_RAW_DIR_H

#ifdef __cplusplus
extern "C" {
#endif

struct RawDir;

typedef struct RawDir RawDir;

const char *OH_ResourceManager_GetRawFileName(RawDir *rawDir, int index);

int OH_ResourceManager_GetRawFileCount(RawDir *rawDir);

void OH_ResourceManager_CloseRawDir(RawDir *rawDir);

#ifdef __cplusplus
};
#endif

/** @} */
#endif // GLOBAL_RAW_DIR_H
