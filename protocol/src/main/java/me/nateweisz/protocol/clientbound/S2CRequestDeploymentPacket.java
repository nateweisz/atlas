package me.nateweisz.protocol.clientbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

import java.security.PublicKey;
import java.util.UUID;

public class S2CRequestDeploymentPacket implements Packet {
    private final UUID deploymentId; // used for the callback when it is deployed.
    private final String codeProvider; // prob only `git` for now
    private final String path; // the path (ex. nateweisz/atlas) likely only git support for now.
    private final String commitHash; // TODO: in the future this should likely be a metadata tag when we support non git stuff.
    
    public S2CRequestDeploymentPacket(UUID deploymentId, String codeProvider, String path, String commitHash) {
        // MAJMOR ISSUE IN CURRENT PACKET SYSTEm, WE DONT KNOW WHAT LENGTH ALOT OF THIS STUFF WILL BE LIKE HOW MUCH TO OFFSET THE getString call.
        this.deploymentId = deploymentId;
        this.codeProvider = codeProvider;
        this.path = path;
        this.commitHash = commitHash;
    }
    
    public S2CRequestDeploymentPacket(Buffer buffer) {
        this.deploymentId = UUID.fromString(buffer.getString(1, 37));
        this.codeProvider = buffer.getString(2, 3);
        this.path = buffer.getString(3, buffer.length());
        this.commitHash = buffer.getString(4, buffer.length());
    }
    
    @Override 
    public void serialize(Buffer buffer) {
        buffer.appendString(deploymentId.toString());
        buffer.appendString(codeProvider);
        buffer.appendString(path);
        buffer.appendString(commitHash);
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
