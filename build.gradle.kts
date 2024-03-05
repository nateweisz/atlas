plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        java {
            endWithNewline()
            removeUnusedImports()
            eclipse("4.19").configFile("${rootProject.rootDir}/meta/formatting/google-style-eclipse.xml")
            // stolen config from tj bot :))
        }
    }
}