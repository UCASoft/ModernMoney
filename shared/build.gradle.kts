plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
}

kotlin {
    jvmToolchain(21)
    jvm()
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)

                implementation(compose.materialIconsExtended)

                implementation(libs.adaptive)
                implementation(libs.adaptive.layout)
                implementation(libs.adaptive.navigation)
                implementation(libs.adaptive.navigation.suite)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.compose.backhandler)
            }
        }
    }
}

android {
    namespace = "com.ucasoft.modernMoney"
    compileSdk = 36
}