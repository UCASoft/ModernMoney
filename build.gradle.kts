plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}

allprojects {
    group = "com.ucasoft.modern-money"

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    version = "0.0.1"
}