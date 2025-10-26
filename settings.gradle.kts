pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://snapshots-repo.kordex.dev")
        maven("https://releases-repo.kordex.dev")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "dolbit-normalno"
