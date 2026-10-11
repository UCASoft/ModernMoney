pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "modern-money"

include(":desktop")
include(":plugin-interface-aidl")
include(":phone")
include(":shared")