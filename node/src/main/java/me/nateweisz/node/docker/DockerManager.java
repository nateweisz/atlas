package me.nateweisz.node.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import me.nateweisz.node.code.ICodeProvider;
import me.nateweisz.node.code.impl.GitCodeProvider;
import me.nateweisz.node.registry.IDockerRegistry;

import java.time.Duration;
import java.util.Set;

public class DockerManager {
    private final DockerClient client;
    private final ICodeProvider gitProvider;
    
    public DockerManager(IDockerRegistry registry) {
        DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .build();
        
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(clientConfig.getDockerHost())
                .sslConfig(clientConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        
        client = DockerClientImpl.getInstance(clientConfig, httpClient);
        gitProvider = new GitCodeProvider();
        // I will prob need to pass in a github access token and figure out how to access the code when we get to this.
    }

    public DockerClient getClient() {
        return client;
    }
}
