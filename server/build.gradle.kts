import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*


plugins {
    id("me.nateweisz.atlas-conventions")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

val vertxVersion = "4.5.5"
val junitJupiterVersion = "5.10.2"

val mainVerticleName = "me.nateweisz.server.Main"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-json-schema")
    implementation("io.vertx:vertx-auth-jwt")
<<<<<<< HEAD
    implementation("io.vertx:vertx-web-validation")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
=======
>>>>>>> c4fe45099d5f43b0d517d2cd2934a7dd8a35a421
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

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
    args = listOf(
        "run",
        mainVerticleName,
        "--redeploy=$watchForChange",
        "--launcher-class=$launcherClassName",
        "--on-redeploy=$doOnChange"
    )
}

application {
    mainClass = launcherClassName
}