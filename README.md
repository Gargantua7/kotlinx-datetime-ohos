# kotlinx-datetime-ohos-compat
> 源官方仓库: [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)

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

* 需要在 DevEco 项目中的 `CMakeList.txt` 中链接 `libtime_service_ndk.so`
* 不支持且移除了 kotlinx-serialization 相关代码
* 由于鸿蒙支持的时区有限，本仓不保证全球时区可用，参见[支持的系统时区-华为HarmonyOS开发者](https://developer.huawei.com/consumer/cn/doc/harmonyos-references/js-apis-date-time#%E6%94%AF%E6%8C%81%E7%9A%84%E7%B3%BB%E7%BB%9F%E6%97%B6%E5%8C%BA)；目前已测试 `Asia/Shanghai` 时区正常可用
* 由于和官方仓代码已存在区别，暂无法与官方库直接混用