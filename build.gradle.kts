import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

    application

    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"

    id("com.github.johnrengelman.shadow") version "8.1.1"

}

val kordVersion: String by project

group = "dev.h4kt"
version = "2.0.0"

application {
    mainClass.set("dev.h4kt.pivosound.AppKt")
}

repositories {

    mavenCentral()
    google()

    maven("https://jitpack.io")
    maven("https://maven.lavalink.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")

}

dependencies {

    implementation("dev.arbjerg:lavaplayer:0eaeee195f0315b2617587aa3537fa202df07ddc-SNAPSHOT")
//    implementation("com.github.walkyst:lavaplayer-fork:1.4.0")

    implementation(libs.kotlinx.serialization.hocon)

    implementation(libs.kord.extensions)
    implementation(libs.slf4j)

}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("pivo-sound-all.jar")
}

tasks.run.get().apply {
    workingDir = File("run")
}
