import resourceManager from "@ohos.resourceManager";

export const add: (a: number, b: number) => number;
export const initNativeResourceManager: (mgr: resourceManager.ResourceManager) => void;
export const getCurrentDateTime: () => string;
export const getCurrentTimeZoneId: () => string;
export const getCurrentTimestamp: () => number;
export const getTimeZoneOffset: () => number;