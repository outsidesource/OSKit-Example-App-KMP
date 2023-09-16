plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "com.outsidesource.oskitExample.common"
version = "1.0-SNAPSHOT"

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "common"
            isStatic = true
        }
    }

    jvm("desktop") {
        jvmToolchain(11)
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.outsidesource:oskit-kmp:4.0.0")
                api("io.insert-koin:koin-core:3.4.3")
                api("co.touchlab:kermit:1.1.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api("org.jetbrains.kotlinx:atomicfu:0.21.0")
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
                api("androidx.core:core-ktx:1.12.0")
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting
        val desktopTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}