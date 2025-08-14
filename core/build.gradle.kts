@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

val group by rootProject.properties
val version by rootProject.properties

kotlin {
    ohosArm64()

    applyDefaultHierarchyTemplate {
        common {
            group("commonKotlin") {
                withOhosArm64()
            }
        }
    }
}

mavenPublishing {

    coordinates(group.toString(), "kotlinx-datetime", version.toString())

    pom {
        name = "kotlinx-datetime"
        description = "Kotlin Datetime Library - ohos compat"
        url = "https://github.com/Kotlin/kotlinx-datetime"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }

        developers {
            developer {
                name = "Gargantua7"
                url = "https://github.com/Gargantua7"
                email = "gargantua7@qq.com"
            }
        }

        scm {
            url = "https://github.com/Kotlin/kotlinx-datetime"
        }
    }
}