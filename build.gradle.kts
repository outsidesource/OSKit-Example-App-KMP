plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

// To get compose compiler metrics run: ./gradlew :desktop:run -PcomposeCompilerReports=true
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs.add("-P plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${projectDir.resolve("/build").absolutePath}/compose_compiler")
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs.add("-P plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${projectDir.resolve("/build").absolutePath}/compose_compiler")
            }
        }
    }
}