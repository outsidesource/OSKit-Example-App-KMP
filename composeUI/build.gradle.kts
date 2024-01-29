plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.5.11"
    id("com.android.library")
    id("co.touchlab.skie") version "0.6.1"
}

group = "com.outsidesource.oskitExample.composeUI"
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
            baseName = "composeUI"
            isStatic = true // https://youtrack.jetbrains.com/issue/KT-42254
            export("com.outsidesource:oskit-kmp:4.2.0")
            export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            export(project(":common"))
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                api("com.outsidesource:oskit-kmp:4.2.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                api("com.outsidesource:oskit-compose:3.2.1")
                api("com.squareup.okio:okio:3.7.0")
                api(project(":common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.compose.ui:ui:1.6.0")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.core:core-ktx:1.12.0")
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
    }
}

android {
    namespace = "com.outsidesource.oskitExample.composeUI"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        kotlin.jvmToolchain(17)
    }
}