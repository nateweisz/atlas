package me.nateweisz.node.socket.listeners;

import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.node.code.ICodeProvider;
import me.nateweisz.node.code.impl.GitCodeProvider;
import me.nateweisz.node.docker.BuildSpec;
import me.nateweisz.node.docker.DockerManager;
import me.nateweisz.protocol.clientbound.S2CRequestDeploymentPacket;
import me.nateweisz.protocol.eventbus.PacketListener;

public class DeploymentRequestListener implements PacketListener<S2CRequestDeploymentPacket> {
    private final DockerManager dockerManager;

    public DeploymentRequestListener(DockerManager dockerManager) {
        this.dockerManager = dockerManager;
    }

    @Override
    public Class<S2CRequestDeploymentPacket> getPacketType() {
        return S2CRequestDeploymentPacket.class;
    }

    @Override
    public void handle(S2CRequestDeploymentPacket packet, ServerWebSocket serverWebSocket) {
        // todo: build and compile docker image, check for already existing builds with current
        // commit hash :D
        ICodeProvider codeProvider = switch (packet.getCodeProvider()) {
            case "git" -> new GitCodeProvider();
            default -> throw new IllegalStateException(
                    "Unexpected value: " + packet.getCodeProvider());
        };

        if (!codeProvider.validateRepository(packet.getPath())) {
            System.out.println("Repo was found invalid: " + packet.getPath());
            return;
        }

        BuildSpec build = new BuildSpec(packet.getPath(),
                /* TODO: Unhardcode this but for now it will be like this!! */ "Astro");
        dockerManager.queueBuild(build);
    }
}
