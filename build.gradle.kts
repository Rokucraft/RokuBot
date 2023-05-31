plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    maven {
        url = uri("https://m2.dv8tion.net/releases")
        name = "m2-dv8tion"
    }
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.19") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.kohsuke:github-api:1.314")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
}



tasks {
    compileJava {
        options.encoding = "UTF-8"

    }
    shadowJar {
        archiveClassifier.set("")
        minimize {
            exclude(dependency("io.jsonwebtoken:jjwt-impl:.*"))
            exclude(dependency("io.jsonwebtoken:jjwt-jackson:.*"))
        }
    }
}