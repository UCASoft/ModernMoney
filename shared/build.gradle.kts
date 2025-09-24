plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
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

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(libs.adaptive)
                implementation(libs.adaptive.layout)
                implementation(libs.adaptive.navigation)
                implementation(libs.adaptive.navigation.suite)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.compose.backhandler)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.lifecycle.runtime.compose)
            }
        }

        val jvmMain by getting {
            dependencies {
                runtimeOnly(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

android {
    namespace = "com.ucasoft.modernMoney"
    compileSdk = 36
}

dependencies {
    add("kspJvm", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}