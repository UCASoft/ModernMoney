plugins {
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
    compileSdk = 37

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
    buildToolsVersion = "37.0.0"
}

val syncKmpStrings = tasks.register<Copy>("syncKmpStrings") {
    description = "Copy shared composeResources strings into Android res"

    from(
        rootProject.file(
            "shared/src/commonMain/composeResources/values/common.xml"
        )
    )
    into(layout.projectDirectory.dir("src/main/res/values"))

    rename {
        val name = it.substringBeforeLast(".")
        "${name}_from_shared.xml"
    }
}

afterEvaluate {
    tasks.findByName("preBuild")?.dependsOn(syncKmpStrings)
}