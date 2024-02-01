package me.nateweisz.node;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import me.nateweisz.node.socket.ServerWebsocket;

// TODO: Add options to only accept nodes from certain ips
public class Node extends AbstractVerticle {


    public static void main(String[] args) {
        // Start the verticle deployment manually
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Node());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        init();
    }

    private void init() {
        HttpClientOptions options = new HttpClientOptions().setSsl(false);
        HttpClient client = vertx.createHttpClient(options);
        
        String secret = "secretTEST123";
        
        client.webSocket(8080, "localhost", "/", new ServerWebsocket(secret));
    }
}
