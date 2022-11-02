import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

val kordVersion: String by project

group = "io.h4kt"
version = "1.0"

repositories {

    mavenCentral()

    maven {
        url = uri("https://m2.dv8tion.net/releases")
    }

}

dependencies {

    implementation("com.sedmelluq:lavaplayer:1.3.77")

    implementation("dev.kord:kord-core:$kordVersion") {
        capabilities {
            requireCapability("dev.kord:core-voice:$kordVersion")
        }
    }

}

application {
    mainClass.set("MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}