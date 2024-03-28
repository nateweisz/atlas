package me.nateweisz.server;

import io.vertx.core.Vertx;
import me.nateweisz.server.auth.AuthVerticle;
import me.nateweisz.server.http.HttpVerticle;

public class Main {

    public static void main(String[] args) {
        // Start the vertical deployment manually
        Vertx vertx = Vertx.vertx();

        // only need to run once instance of http server.
        vertx.deployVerticle(new HttpVerticle());
    }
}
