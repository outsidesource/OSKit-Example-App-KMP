@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.io.FileInputStream
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.skie)
}

val versionProps = Properties().apply { load(FileInputStream(File(rootProject.rootDir, "version.properties"))) }
val versionName = versionProps["version"]?.toString() ?: "0.0.0"
val versionBuild = versionProps["build"]?.toString() ?: "0"

val lwjglVersion = "3.3.3"

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } -> {
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) }) {
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            } else {
                "natives-linux"
            }
        }
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } -> {
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        }
        arrayOf("Windows").any { name.startsWith(it) } -> {
            if (arch.contains("64")) {
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            } else {
                "natives-windows-x86"
            }
        }
        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }

    androidTarget()
    jvm("desktop")

    wasmJs {
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
        }

        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true // https://youtrack.jetbrains.com/issue/KT-42254
            export(libs.oskit.kmp)
            export(libs.kotlinx.coroutines.core)
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val desktopMain by getting
        val androidInstrumentedTest by getting

        commonMain {
            generateKmpBuildInfo(layout.buildDirectory.asFile.get(), versionName, versionBuild)
            kotlin.srcDir("${layout.buildDirectory.asFile.get().absolutePath}/generated/com/outsidesource/kmpbuild")
        }

        commonMain.dependencies {
            api(libs.oskit.kmp)
            api(libs.kotlinx.coroutines.core)
            api(libs.koin.core)
            api(libs.kotlinx.datetime)
            api(libs.atomicfu)
            api(libs.kotlinx.serialization.json)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.material.icons)
            implementation(libs.oskit.compose)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.ui)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.junit)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
            runtimeOnly("org.lwjgl:lwjgl-tinyfd:$lwjglVersion:$lwjglNatives")
        }
    }
}

android {
    namespace = "com.outsidesource.oskitExample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        kotlin.jvmToolchain(17)
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "OSKit-KMP-Example"
            packageVersion = "1.0.0"
        }
    }
}

private fun generateKmpBuildInfo(buildDir: File, versionName: String, versionBuild: String) {
    val directory = File("${buildDir}/generated/com/outsidesource/kmpbuild")
    directory.mkdirs()

    File(directory, "KmpBuildInfo.kt").bufferedWriter().use {
        it.write("""
            package com.outsidesource.kmpbuild
            
            class KmpBuildInfo {
                companion object {
                    const val version: String = "$versionName"
                    const val build: String = "$versionBuild"
                }
            }
        """.trimIndent())
    }
}