plugins {
    alias(libs.plugins.android.library)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.ucasoft.modernMoney.plugin.interfaces.aidl"
    compileSdk = 37
    defaultConfig {
        minSdk = 29
        version = "0.0.1"
    }

    buildFeatures {
        aidl = true
    }
    buildToolsVersion = "37.0.0"
}
