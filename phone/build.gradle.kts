plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(21)
    dependencies {
        implementation(project(":shared"))
        implementation(libs.androidx.activity.compose)
        androidTestImplementation(libs.androidx.ui.test.android)
        androidTestImplementation(libs.androidx.ui.test.manifest)
        androidTestImplementation(libs.androidx.test.runner)
        androidTestImplementation(libs.androidx.test.rules)
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
    buildToolsVersion = "36.1.0"
}