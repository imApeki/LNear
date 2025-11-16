import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "2.2.20"
}

group = "im.apeki"
version = "1.0.0"

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.25.0")
}

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}