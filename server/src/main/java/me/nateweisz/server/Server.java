package me.nateweisz.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import me.nateweisz.server.node.NodeWebsocket;

import java.util.logging.Logger;

// TODO: Add options to only accept nodes from certain ips
public class Server extends AbstractVerticle {
    
    private static Logger LOGGER;
    private HttpServer httpServer;
    
    public static void main(String[] args) {
        // Start the verticle deployment manually
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        LOGGER = Logger.getLogger(Server.class.getName());
        
        LOGGER.info("Setting up the HTTP server!");
        setupHttpServer(startPromise);
    }
    
    private void setupHttpServer(Promise<Void> startPromise) {
        String secret = "secretTEST123";
        httpServer = vertx.createHttpServer()
                .webSocketHandler(new NodeWebsocket(secret))
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        LOGGER.info("HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }
}
