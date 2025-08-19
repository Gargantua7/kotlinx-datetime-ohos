@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    ohosArm64 {
        binaries.sharedLib {

            baseName = "kn"

            freeCompilerArgs += "-Xadd-light-debug=enable"
            freeCompilerArgs += "-Xbinary=sourceInfoType=libbacktrace"

        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(projects.core)
        }

    }
}

arrayOf("debug", "release").forEach { type ->
    tasks.register<Copy>("publish${type.capitalized()}BinariesToHarmonyApp") {
        group = "harmony"
        dependsOn("link${type.capitalized()}SharedOhosArm64")
        into(rootProject.file("ohosApp"))
        from("build/bin/ohosArm64/${type}Shared/libkn_api.h") {
            into("entry/src/main/cpp/include/")
        }
        from("build/bin/ohosArm64/${type}Shared/libkn.so") {
            into("entry/libs/arm64-v8a/")
        }
    }
}