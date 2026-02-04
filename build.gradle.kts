plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    id("maven-publish")
}

val unpick_version = "1.3.0"

configurations.all {
    resolutionStrategy {
        force("net.fabricmc:unpick:$unpick_version")
    }
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_api_version: String by project
val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project

loom {
    runs {
        named("client") {
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}

version = mod_version
group = maven_group
base.archivesName.set(archives_base_name)

repositories {
    maven { url = uri("https://maven.fabricmc.net/") }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}