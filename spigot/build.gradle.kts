plugins {
    id("com.gradleup.shadow") version "8.3.3"
}

apply(plugin = "com.gradleup.shadow")

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

val jetbrainsAnnotations = "25.0.0"
val spigot = "1.17-R0.1-SNAPSHOT"

dependencies {
    implementation(project(":api")) {
        exclude(group = "com.j256.ormlite")
        exclude(group = "org.jetbrains")
    }
    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")
    compileOnly("org.spigotmc:spigot-api:${spigot}")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    assemble {
        dependsOn(shadowJar)
    }
}