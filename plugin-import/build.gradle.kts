plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvmToolchain(21)
    dependencies {
        implementation(project(":plugin-interface"))
        implementation(libs.kotlinx.serialization.json)
    }
}

android {
    namespace = "com.ucasoft.modernMoney.plugin.imports"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.ucasoft.modernMoney.plugin.imports"
        minSdk = 29
        version = 1
        versionName = "0.0.1"
    }

    buildToolsVersion = "37.0.0"
}
