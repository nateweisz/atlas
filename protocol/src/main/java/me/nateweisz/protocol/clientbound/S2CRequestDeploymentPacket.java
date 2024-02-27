package me.nateweisz.protocol.clientbound;

import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;

import java.util.UUID;

public class S2CRequestDeploymentPacket implements Packet {
    private final UUID deploymentId; // used for the callback when it is deployed and all other future sent packets.
    private final String codeProvider; // prob only `git` for now
    private final String path; // the path (ex. nateweisz/atlas) likely only git support for now.
    private final String commitHash; // TODO: in the future this should likely be a metadata tag when we support non git stuff.

    public S2CRequestDeploymentPacket(UUID deploymentId, String codeProvider, String path, String commitHash) {
        this.deploymentId = deploymentId;
        this.codeProvider = codeProvider;
        this.path = path;
        this.commitHash = commitHash;
    }

    public S2CRequestDeploymentPacket(WrappedBuffer buffer) {
        this.deploymentId = UUID.fromString(buffer.nextString());
        this.codeProvider = buffer.nextString();
        this.path = buffer.nextString();
        this.commitHash = buffer.nextString();
    }

    @Override
    public void serialize(WrappedBuffer buffer) {
        buffer.writeString(deploymentId.toString());
        buffer.writeString(codeProvider);
        buffer.writeString(path);
        buffer.writeString(commitHash);
    }

    public UUID getDeploymentId() {
        return deploymentId;
    }

    public String getCodeProvider() {
        return codeProvider;
    }

    public String getPath() {
        return path;
    }

    public String getCommitHash() {
        return commitHash;
    }
}
