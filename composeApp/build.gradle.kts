import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.skie)
}

version = "1.0-SNAPSHOT"

kotlin {
    androidTarget {
        jvmToolchain(17)
    }
    jvm("desktop") {
        jvmToolchain(17)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true // https://youtrack.jetbrains.com/issue/KT-42254
            export("com.outsidesource:oskit-kmp:4.4.1")
            export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            export(project(":common"))
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val desktopMain by getting
        val desktopTest by getting
        val androidInstrumentedTest by getting

        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            api(libs.oskit.kmp)
            api(libs.kotlinx.coroutines.core)
            api(libs.oskit.compose)
            api(libs.okio)
            api(project(":common"))
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
            implementation(project(":common"))
            implementation(compose.desktop.currentOs)
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
