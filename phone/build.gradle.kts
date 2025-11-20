plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
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
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.ui.test.android)
                implementation(libs.androidx.ui.test.manifest)
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.rules)
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
}