plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(21)
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

android {
    namespace = "com.ucasoft.modernMoney"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ucasoft.modernMoney"
        minSdk = 29
        version = 1
        versionName = "0.0.1"
    }

    buildFeatures {
        compose = true
    }
}