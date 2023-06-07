import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.4.0"
    id("com.android.library")
}

group = "com.outsidesource.oskitExample.composeUI"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        val main by compilations.getting {
            compilerOptions.configure {
                jvmToolchain(11)
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.preview)
                implementation("androidx.compose.ui:ui:1.4.3")
                implementation("com.outsidesource:oskit-compose:2.0.0")
                implementation(project(":common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting
        val desktopTest by getting
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        kotlin.jvmToolchain(11)
    }
}