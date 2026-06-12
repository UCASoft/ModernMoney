plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(21)
    jvm()
    /*linuxX64()
    mingwX64()*/
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.components.resources)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose {
    resources {
        customDirectory(
            "jvmMain",
            provider { layout.projectDirectory.dir("src/jvmMain/resources") }
        )
    }
}