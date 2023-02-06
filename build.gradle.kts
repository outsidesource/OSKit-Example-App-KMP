allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/outsidesource/OSKit-KMP")
            credentials {
                val credentialProperties = java.util.Properties()
                if (System.getenv("OS_DEVELOPER") == null) {
                    File(project.rootDir, "credential.properties").reader().use { stream -> credentialProperties.load(stream) }
                }

                username = System.getenv("OS_DEVELOPER") ?: credentialProperties["username"].toString()
                password = System.getenv("OS_TOKEN") ?: credentialProperties["password"].toString()
            }
        }
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}