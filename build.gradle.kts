import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
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

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.4.1")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    implementation("dev.kord:kord-core:$kordVersion") {
        capabilities {
            requireCapability("dev.kord:core-voice:$kordVersion")
        }
    }

}

application {
    mainClass.set("io.h4kt.pivosound.MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}