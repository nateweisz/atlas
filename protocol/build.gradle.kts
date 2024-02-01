plugins {
    id("me.nateweisz.atlas-conventions")
}

val vertxVersion = "4.5.2"

dependencies {
    compileOnly(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    compileOnly("io.vertx:vertx-web")
}