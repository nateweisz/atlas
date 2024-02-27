package me.nateweisz.node.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import me.nateweisz.node.code.ICodeProvider;
import me.nateweisz.node.code.impl.GitCodeProvider;
import me.nateweisz.node.registry.IDockerRegistry;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerManager {
    private final File BUILD_DIR = new File("/opt/atlas/builds/");

    private final DockerClient client;
    private final ICodeProvider gitProvider;
    private final HashMap<String, BuildSpec> specs;

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
        specs = new HashMap<>();
        // I will prob need to pass in a github access token and figure out how to access the code when we get to this.
        // TODO: restructure the git related items from here into a seperate one.
    }

    public void queueBuild(BuildSpec build) {
        // 1. Clone it locally and add it as a volume
        // 2. Clone it while inside of docker container (for this I will just make it work w/ public repo's for now)
        // Clonig it inside docker container's is not possible because we use a docker in docker image

        try {
            Git
                    .cloneRepository()
                    .setURI("https://github.com/" + build.getPath())
                    .setDirectory(new File("/opt/atlas/volumes/" + build.getPath()))
                    .call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1. Clone repo
        // 2. Delete any existing Dockerfile
        // 3. Throw the Dockerfile from dockerfiles/frontend/Astro into the base of the repo

        CreateContainerResponse container = client.createContainerCmd("base-atlas:latest")
                .withCmd("cd /opt/atlas/volumes/" + build.getPath() + " && docker build -t .")
                .withLabels(
                        Map.of("atlas-type", "build")
                )
                .withVolumes(
                        new Volume("/opt/atlas/volumes/" + build.getPath())
                )
                .withBinds()
                .withHostConfig(
                        HostConfig.newHostConfig()
                        // TODO: limit memory usage here (not sure what the memory type is, Ex. mb or bytes ect
                )
                .exec();

        client.startContainerCmd(container.getId())
                .exec();
    }

    public void onBuildComplete(String containerId) {
        BuildSpec buildSpec = specs.get(containerId);

        if (buildSpec == null) {
            throw new RuntimeException("A build container was cleaned up but no build spec was found in DockerManager#specs");
        }

        startDockerContainer(buildSpec);
    }

    public void startDockerContainer(BuildSpec build) {
        String containerId = client.createContainerCmd(gitProvider.getDockerImageTag(build))
                .withExposedPorts(List.of(ExposedPort.tcp(3000))) // exposed port will always be 3000 for now
                .exec()
                .getId();

        client.startContainerCmd(containerId)
                .exec();


    }

    private File getDockerFilePerType(String type) {
        return new File(
                DockerManager.class.getClassLoader()
                        .getResource("/dockerfiles/frontend/" + type)
                        .getFile()
        );
    }

    public DockerClient getClient() {
        return client;
    }
}
