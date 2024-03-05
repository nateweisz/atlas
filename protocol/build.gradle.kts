plugins {
    id("me.nateweisz.atlas-conventions")
}

val vertxVersion = "4.5.4"

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
}