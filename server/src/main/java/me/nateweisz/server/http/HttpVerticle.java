package me.nateweisz.server.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.json.schema.SchemaParser;
import me.nateweisz.server.auth.AuthController;

public class HttpVerticle extends AbstractVerticle {
    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {
        // this is the router that will be passed into all the different controllers.
        Router router = Router.router(vertx);
        new AuthController(vertx, router);

        server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onFailure(startPromise::fail)
                .result();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        server.close(stopPromise);
    }
}
