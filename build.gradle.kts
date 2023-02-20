import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

val kordVersion: String by project

group = "io.h4kt"
version = "1.0"

application {
    mainClass.set("io.h4kt.pivosound.MainKt")
}

repositories {

    mavenCentral()

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.4.1")
    implementation("com.github.walkyst:lavaplayer-fork:1.3.99.2")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    implementation("dev.kord:kord-core:$kordVersion") {
        capabilities {
            requireCapability("dev.kord:core-voice:$kordVersion")
        }
    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("pivo-sound-all.jar")
}
