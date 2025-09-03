# kotlinx-datetime-ohos-compat
> 源官方仓库: [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)

***

**fork 自官方仓库支持`kotlinx-serialization`的完整版本：[kotlinx-datetime/ohos-compat](https://github.com/Gargantua7/kotlinx-datetime)**
> **此仓库将废弃**

***

基于 kotlinx-datetime(0.6.2) 和 [KuiklyBase-kotlin](https://github.com/Tencent-TDS/KuiklyBase-kotlin) 构建目标平台 ohosArm64 的时间库

由于目前不支持 kotlinx-serialization，所以未直接 fork 官方仓仅复制所需源集并且移除了 kotlinx-serialization 依赖的相关代码

***

### core 模块中的源集对应官方仓

| 本仓                                           | 官方仓                                                       |
| ---------------------------------------------- | ------------------------------------------------------------ |
| [commonMain](/core/src/commonMain)             | [common](https://github.com/Kotlin/kotlinx-datetime/tree/v0.6.2/core/common/src) |
| [commonKotlinMain](/core/src/commonKotlinMain) | [commonKotlin](https://github.com/Kotlin/kotlinx-datetime/tree/v0.6.2/core/commonKotlin/src) |

[ohosArm64Main](/core/src/ohosArm64Main) 为平台实现源集

***

### 使用注意

* 需要在 DevEco 项目中的 `CMakeList.txt` 中额外链接 `libtime_service_ndk.so` `librawfile.z.so`

* 不支持且移除了 kotlinx-serialization 相关代码

* 由于和官方仓代码已存在区别，暂无法与官方库直接混用

* **时区和 tzdb 相关**

  * 由于鸿蒙有关时区的 API 支持有限，`sample` 采用了嵌入 `tzdb` 的方式，`tzdb` 编译方式请参考[`kotlinx-datetime/timezones`](https://github.com/Kotlin/kotlinx-datetime/tree/master/timezones/full)，也可自取其下`tzdb`目录编译后产物

  * 需要将 `tzdb` 编译后产物复制到 DevEcoStudio 项目中 KMP 二进制产物所在模块的 `resource/rawfile`下，如`sample` 和 `ohosApp`所示

  * 需要在 `UIAbility` 的 `onCreate` 中初始化`NativeResourceManager`并提供给`core` 中的`initNativeResourceManagerForDatetime`以便访问 tzdb  文件

    ```kotlin
    // kotlin side
    @CName("initNativeResourceManager")
    fun initNativeResourceManager(env: napi_env, jsResMgr: napi_value) {
      initNativeResourceManagerForDatetime(OH_ResourceManager_InitNativeResourceManager(env, jsResMgr))
    }
    ```

    ```cpp
    // napi_init.cpp
    static napi_value InitNativeResourceManager(napi_env env, napi_callback_info info)
    {
        size_t argc = 1;
        napi_value jsResMgr;
        napi_get_cb_info(env, info, &argc, &jsResMgr, nullptr, nullptr);
        initNativeResourceManager(env, jsResMgr);
        return nullptr;
    }
    ```

    ```typescript
    // index.d.ts
    export const initNativeResourceManager: (mgr: resourceManager.ResourceManager) => void;
    
    // EntryAblitiy.ets
    export default class EntryAbility extends UIAbility {
      onCreate(want: Want, launchParam: AbilityConstant.LaunchParam): void {
        initNativeResourceManager(this.context.resourceManager);
      }
    }
    ```
    > 在 ohosApp 中测试: 添加`tzdb`产物后，编译后`app`发布文件体积仅增大 0.1 MB
