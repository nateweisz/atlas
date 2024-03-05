plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    this.group = "me.nateweisz"
    this.version = file("${rootProject.rootDir}/atlas.version").readText()

    spotless {
        java {
            endWithNewline()
            removeUnusedImports()
            formatAnnotations()
            eclipse("4.19").configFile("${rootProject.rootDir}/meta/formatting/google-style-eclipse.xml")
            // stolen config from tj bot :))
        }
    }
}