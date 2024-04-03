plugins {
    application
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.google.cloud.tools.jib") version "3.4.2"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
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
    implementation("net.dv8tion:JDA:5.0.0-beta.21") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("org.kohsuke:github-api:1.321")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("com.google.dagger:dagger:2.51.1")
    ksp("com.google.dagger:dagger-compiler:2.51.1")
}

jib.to.image = "ghcr.io/rokucraft/rokubot:latest"
