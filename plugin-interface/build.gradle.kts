plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
}

kotlin {
    jvmToolchain(21)
    jvm()
    android {
        namespace = "com.ucasoft.modernMoney.plugin.interfaces"
        compileSdk = 37
        minSdk = 29
        androidResources {
            enable = true
        }
        buildToolsVersion = "37.0.0"
    }
    sourceSets {
        commonMain {
            dependencies {
            }
        }

        commonTest {
            dependencies {
            }
        }

        jvmMain {
            dependencies {
            }
        }

        jvmTest {
            dependencies {
            }
        }

        androidMain {
            dependencies {
                implementation(project(":plugin-interface-aidl"))
            }
        }
    }
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

gradle.taskGraph.whenReady {
    allTasks
        .filter { it.path == "${project.path}:compileCommonMainKotlinMetadata" }
        .forEach { task ->
            task.enabled = true
            task.setOnlyIf { true }
        }
}