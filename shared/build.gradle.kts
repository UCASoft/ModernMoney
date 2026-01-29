import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvmToolchain(21)
    jvm()
    androidLibrary {
        namespace = "com.ucasoft.modernMoney"
        compileSdk = 36
        androidResources {
            enable = true
        }
    }
    sourceSets {
        @OptIn(ExperimentalComposeLibrary::class)
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

                implementation(libs.preference)
                api(libs.settings)
                api(libs.settings.coroutines)
                api(libs.settings.observable)

                implementation(libs.kotlinx.serialization.json)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)

                api(compose.uiTest)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.mock)
            }
        }

        val jvmMain by getting {
            dependencies {
                runtimeOnly(libs.kotlinx.coroutines.swing)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.ui.test.android)
            }
        }
    }
}

dependencies {
    add("kspJvm", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}