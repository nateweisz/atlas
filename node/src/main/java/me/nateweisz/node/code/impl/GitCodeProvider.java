package me.nateweisz.node.code.impl;

import io.vertx.core.json.JsonObject;
import me.nateweisz.node.code.ICodeProvider;
import me.nateweisz.node.docker.BuildSpec;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitCodeProvider implements ICodeProvider {
    private final HttpClient httpClient;
    
    public GitCodeProvider() {
        httpClient = HttpClient.newHttpClient();
    }
    
    @Override
    public String getDockerImageTag(BuildSpec buildSpec) {
        String[] split = buildSpec.getPath().split("/");
        
        if (split.length < 2) {
            return null;
        }
        
        return "github_" + split[0] + "_" + split[1] + ":latest";
    }

    @Override
    public boolean validateRepository(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/" + path))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert response != null;
        JsonObject json = new JsonObject(response.body());
        
        return json.getString("message") == null;
    }
}
