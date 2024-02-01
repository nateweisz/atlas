package me.nateweisz.node.socket.listeners;

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
    public void handle(S2CRequestDeploymentPacket packet) {
        // todo: build and compile docker image, check for already existing builds with current commit hash :D
    }
}
