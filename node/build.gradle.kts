import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("me.nateweisz.atlas-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val vertxVersion = "4.5.2"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "me.nateweisz.node.Node"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

dependencies {
    implementation(project(":protocol"))
    implementation("org.apache.commons:commons-text")
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-json-schema")
    implementation("com.github.docker-java:docker-java:3.3.4")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.4")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")}

tasks.withType<ShadowJar> {
    archiveClassifier.set("fat")
    manifest {
        attributes(mapOf("Main-Verticle" to mainVerticleName))
    }
    mergeServiceFiles()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
}

tasks.withType<JavaExec> {
    args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

application {
    mainClass = launcherClassName
}

// Define the task to copy Dockerfiles into resources
val copyDockerfiles by tasks.registering(Copy::class) {
    from("../dockerfiles") // Source directory
    into("src/main/resources/dockerfiles") // Destination directory within the node project
}

// Ensure that the copyDockerfiles task runs before the processResources task
tasks.named("processResources") {
    dependsOn(copyDockerfiles)
}
