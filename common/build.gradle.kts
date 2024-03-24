plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.skie)
    kotlin("native.cocoapods")
}

group = "com.outsidesource.oskitExample.common"
version = "1.0-SNAPSHOT"

kotlin {
    androidTarget {
        jvmToolchain(17)
    }
    jvm("desktop") {
        jvmToolchain(17)
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    cocoapods {
        version = "1.12.1"
        ios.deploymentTarget = "15"

        pod("AWSCore") {
            version = "2.33.8"
        }
        pod("AWSS3") {
            version = "2.33.8"
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            api(libs.oskit.kmp)
            api(libs.koin.core)
            api(libs.kermit)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            api(libs.atomicfu)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            api(libs.androidx.appcompat)
            api(libs.androidx.core.ktx)
        }
    }
}

android {
    namespace = "com.outsidesource.oskitExample.common"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}