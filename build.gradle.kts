plugins {
    application
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
}

application {
    mainClass = "com.rokucraft.rokubot.MainKt"
}

group = "com.rokucraft"
version = "2.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.1.0") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.5.7")
    implementation("org.kohsuke:github-api:1.324")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

    implementation("com.google.dagger:dagger:2.52")
    ksp("com.google.dagger:dagger-compiler:2.52")
}

jib.to.image = "ghcr.io/rokucraft/rokubot:latest"
