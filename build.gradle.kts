import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {

    application

    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)

    alias(libs.plugins.kordex)
    alias(libs.plugins.shadow)

    alias(libs.plugins.graalvm.python)

}

group = "dev.h4kt"
version = "2.0.0"

repositories {

    mavenCentral()

    maven("https://jitpack.io")
    maven("https://maven.lavalink.dev/releases")
    maven("https://maven.lavalink.dev/snapshots")

}

dependencies {

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.lavaplayer)
    implementation(libs.lavalink.youtube.v2)
    implementation(libs.slf4j)

    implementation(libs.graalvm.polyglot)
    implementation(libs.graalvm.polyglot.python)

    implementation(libs.koin.annotatins)
    ksp(libs.koin.compiler)

    testImplementation(kotlin("test"))
}

application {
    mainClass = "dev.h4kt.pivosound.AppKt"
}

kotlin {
    compilerOptions {
        jvmToolchain {
            languageVersion = JavaLanguageVersion.of(21)
            vendor = JvmVendorSpec.GRAAL_VM
        }

        freeCompilerArgs.addAll(
            "-Xdata-flow-based-exhaustiveness",
            "-Xcontext-sensitive-resolution",
            "-Xcontext-parameters"
        )
    }
}

graalPy {
    packages = setOf(
        "pytube",
        "youtube-search-python",
        "requests"
    )

    community = true
}

kordEx {
    i18n {
        classPackage = "dev.h4kt.pivosound.generated.i18n"
        translationBundle = "english.strings"
    }

    bot {
        voice = true
    }
}

tasks.withType<ShadowJar> {
    isZip64 = true
    archiveFileName = "pivo-sound-all.jar"

    mergeServiceFiles()
}

tasks.test {
    useJUnitPlatform()
}

afterEvaluate {
    tasks.named<JavaExec>("run") {
        workingDir = File("run")
    }

    tasks.named<JavaExec>("runShadow") {
        workingDir = File("run")
    }
}
