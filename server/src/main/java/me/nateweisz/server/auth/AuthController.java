package me.nateweisz.server.auth;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.JsonSchemaOptions;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import me.nateweisz.server.Endpoints;

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

public class AuthController {
    private final Vertx vertx;

    public AuthController(Vertx vertx, Router router) {
        this.vertx = vertx;
        SchemaParser schemaParser = SchemaParser.createDraft7SchemaParser(
                SchemaRouter.create(vertx, new SchemaRouterOptions())
        );

        router.post("/api/*").handler(BodyHandler.create());
        router.post(Endpoints.LOGIN).handler(BodyHandler.create()).handler(this::handleLogin);
        router.post(Endpoints.LOGOUT).handler(this::handleLogout);
        router.post(Endpoints.REFRESH_TOKEN).handler(this::handleRefresh);
        vertx.deployVerticle(new AuthVerticle(router));
    }

    public record LoginRequest(String email, String password) {}

    private ValidationHandler loginValidator() {
        ObjectSchemaBuilder schemaBuilder = objectSchema()
                .property("email", stringSchema())
                .property("password", stringSchema());


        return null;
    }

    private void handleLogin(RoutingContext ctx) {
        LoginRequest request = ctx.body().asPojo(LoginRequest.class);
        if (ctx.body().isEmpty() || request.email == null || request.password == null) {
            System.out.println(":(");
            ctx.failed();
            return;
        }

        vertx.eventBus()
                .request("auth.login", request)
                .onFailure(handler -> {
                    System.out.println("failed the thing");
                    ctx.failed();
                })
                .onSuccess(handler -> {
                    System.out.println(handler.body());
                    ctx.end("Success");
                });
    }

    private void handleLogout(RoutingContext ctx) {

    }

    private void handleRefresh(RoutingContext ctx) {
    }

}
