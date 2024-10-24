plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")

    group = "com.wizardlybump17"
    version = "0.1.0"

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release = 16
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    val junit = "5.11.3"

    dependencies {
        testImplementation(platform("org.junit:junit-bom:${junit}"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }
}