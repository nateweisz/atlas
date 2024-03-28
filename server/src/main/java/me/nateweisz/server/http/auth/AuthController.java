package me.nateweisz.server.http.auth;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.JWTAuthHandler;
import me.nateweisz.server.Endpoints;

public class AuthController {
    private final Vertx vertx;
    private final JWTAuth jwt;

    public AuthController(Vertx vertx, Router router) {
        JWTAuth jwt1;
        this.vertx = vertx;
        jwt1 = null;

        try {
            jwt1 = configureAuth();
        } catch (Exception e) {
            e.printStackTrace();
        }

        jwt = jwt1;
        router.routeWithRegex("/api/*").handler(JWTAuthHandler.create(jwt));
        router.post(Endpoints.LOGIN).handler(this::handleLogin);
        router.post(Endpoints.LOGOUT).handler(this::handleLogout);
        router.post(Endpoints.REFRESH_TOKEN).handler(this::handleRefresh);

        System.out.println("STARGIGN AUTH");
    }

    public record LoginRequest(String email, String password) {}

    private void handleLogin(RoutingContext ctx) {
        ctx.response().putHeader("Content-Type", "text/plain");

        LoginRequest request = ctx.body().asPojo(LoginRequest.class);
        if (ctx.body().isEmpty() || request.email == null || request.password == null) {
            ctx.failed();
            return;
        }
    }

    private void handleLogout(RoutingContext ctx) {

    }

    private void handleRefresh(RoutingContext ctx) {

    }

    private JWTAuth configureAuth() {
        System.out.println("DID JWT");
        return JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setBuffer(
                                """
                                        -----BEGIN PUBLIC KEY-----
                                        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwdQ+8uzgTDrtrovAHjCp
                                        vQmPsUd62GYE8BiQsFzn3SnUGATiAjQeKjupY40Dk4xr+HZWyUFc924zRCitbyMm
                                        Hqq6ZcJddoMfPqqVXl4i09DWXwdKDPKaOhQ4sn+sUbs7cNvRREJdFQlHTQzatFCr
                                        5beGQZ2J5L7+/50HxHP7aqbr4iPa7FqjlxIjeu13UmZ9c6GH3dk/L390DHcMU7XE
                                        nfk4Sz9CQ5eKubs5RGYfCtcmzRnDct3LV6+joRflYP67mBuawKAYxquZTu3GmOjn
                                        YSJitW0gUjyn/zOsFEFXY5bTmwCA0kOpsSMKEV+wGwEdmnhm1mCsPWI4RzJ956y5
                                        zwIDAQAB
                                        -----END PUBLIC KEY-----"""
                        ))
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setBuffer(
                                """
                                        -----BEGIN PRIVATE KEY-----
                                        MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDB1D7y7OBMOu2u
                                        i8AeMKm9CY+xR3rYZgTwGJCwXOfdKdQYBOICNB4qO6ljjQOTjGv4dlbJQVz3bjNE
                                        KK1vIyYeqrplwl12gx8+qpVeXiLT0NZfB0oM8po6FDiyf6xRuztw29FEQl0VCUdN
                                        DNq0UKvlt4ZBnYnkvv7/nQfEc/tqpuviI9rsWqOXEiN67XdSZn1zoYfd2T8vf3QM
                                        dwxTtcSd+ThLP0JDl4q5uzlEZh8K1ybNGcNy3ctXr6OhF+Vg/ruYG5rAoBjGq5lO
                                        7caY6OdhImK1bSBSPKf/M6wUQVdjltObAIDSQ6mxIwoRX7AbAR2aeGbWYKw9YjhH
                                        Mn3nrLnPAgMBAAECggEAHyJ39BnkU5+5ndUuneAvkRZ6luhGTYFhtKwgKGbcCx9q
                                        GsDvXu7pPjEe3fIA9jqoU0e1sWvxl4CDn/oFaznoUyUV05Jbj1WzofjRDSYDjnut
                                        cPlLm0soRpZd9HTkBK2VzY9PDO8ODaV+Zwbeu00kDOkkbzYeOCNinu5QmO1lICB6
                                        rz3z3YPX3NUrZbIBImOA7k3ZRV8eOYMEZG5r4nQ5BxGDF/oWYkZaveDf/bxzGn7g
                                        JVY7w6QRka8uKkUFmvOD6ggPFbactIvbSN8iexiO4JRYEQ6me7XV0sS+G6LZDyHM
                                        NOFYD+8/j1OGkCEUOMkzA6Y6DDLydgfJp8uFQjo+GQKBgQDn8tYNm1CO9nkO16eq
                                        qFd7VCqHKc7eH84JxpPS37f8G/vuMTkfcWn+ofGBCk4wOcuMT6gWynJEWXxLykQy
                                        ZZmshnJPuywrioIe3m/e5UTE+Bnu06zhOMzkLtg5CHJbKamfP000GiWlO3pbrb94
                                        /ExMdTLbTs2sxPxJXet47f4GCQKBgQDV7YNhUSClicyNYEVQLccoLV0puwXDtVNM
                                        bpWU1+IW6P7eyEprntIO7zsSvnpu0/rvH69LPBuGI8YibgxTpNDJdzCTVBKCuONB
                                        ho7vcxNpC/mEkQBv32Csu8ppWrKDH3nhUvHVvIgSkwyaR0ebqohqx0jiTFLNctsQ
                                        jJbJC6x3FwKBgBmm8aVvl+k3uQu7Lf/hvksVc96e8RH/0nmiTJb1xxNKxQICz556
                                        nI5cuqjayurqeWh7Jsved+S3QNfg9sncErLR67KFsMzf9KQVVaoBESn3hmazYltG
                                        POK/i4pDNOKAKRJDnfY1LhSoD1A0Vwi6XnXWQFmgzX5eCsjSakhlicKBAoGAVjwC
                                        A86iLFiZ3jt6dVGewHFY5qN/NV74jwzcuz0y5C1T4BY4+Zzx7Bc/ZtTloZtNweqx
                                        zNAUAESXb/WQfn8nsq6bJCh1tcA1y4okQB3RKz0qfee9l1siKz8f3swyvWyIwu2e
                                        kpwG9gXUxLQLB5L8np5qoN5HWo38BHXHqo9MHE0CgYEA4tDXbpr0PeH7ZJM2tdvh
                                        8gM9GO2LaeYmWcZFyd/5JF8+RmQuOobrhRuvW/PG4fEJFUdESQy255tZfyEfsyVM
                                        /WrRjDEsvjjFX1y3APjVLzM5Ed8qnSOv7lCiVS5OXM2SL7Sht3a3Brs9Yoe2aoDk
                                        o0fiBrp7l9U+722OMe1K/gw=
                                        -----END PRIVATE KEY-----"""
                        ))
        );
    }
}
