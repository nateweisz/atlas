plugins {
    id("me.nateweisz.atlas-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
}

application {
    // Define the main class for the application.
    mainClass.set("me.nateweisz.app.App")
}
