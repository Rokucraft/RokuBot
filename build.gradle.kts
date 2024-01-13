plugins {
    application
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

application {
    mainClass = "com.rokucraft.rokubot.RokuBot"
}

version = "2.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.19") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.kohsuke:github-api:1.318")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

jib.to.image = "ghcr.io/rokucraft/rokubot:latest"

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}
