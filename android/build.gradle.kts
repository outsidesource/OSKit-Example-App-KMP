plugins {
    id("org.jetbrains.compose") version "1.5.11"
    id("com.android.application")
    kotlin("android")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":composeUI"))
    implementation("androidx.activity:activity-compose:1.8.2")
}

android {
    namespace = "com.outsidesource.oskitExample"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.outsidesource.oskitExample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}