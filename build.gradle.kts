plugins {
    application
    kotlin("jvm") version "1.8.22"
    id("com.google.cloud.tools.jib") version "3.3.2"
}

application {
    mainClass.set("com.rokucraft.rokubot.RokuBot")
}

version = "2.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.11") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("org.kohsuke:github-api:1.315")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
}

jib.to.image = "ghcr.io/rokucraft/rokubot:latest"

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}