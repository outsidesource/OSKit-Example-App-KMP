@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.io.FileInputStream
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.skie)
}

val versionProps = Properties().apply { load(FileInputStream(File(rootProject.rootDir, "version.properties"))) }
val versionName = versionProps["version"]?.toString() ?: "0.0.0"
val versionBuild = versionProps["build"]?.toString() ?: "0"

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }

    jvm("desktop")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    wasmJs {
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
        }

        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static(rootDirPath)
                    static(projectDirPath)
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

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.preview)
            implementation(libs.compose.resources)
            implementation(libs.material.icons)
            implementation(libs.oskit.compose)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.outsidesource.oskitExample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
