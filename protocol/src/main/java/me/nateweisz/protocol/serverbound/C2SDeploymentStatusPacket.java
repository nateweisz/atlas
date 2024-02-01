package me.nateweisz.protocol.serverbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

import java.util.UUID;

// for someone reading this and asking why
// we have dif packets with the same fields, it doesnt add
// any extra runtime calculations and makes the code more readable and maintainable.
public class C2SDeploymentStatusPacket implements Packet {
    private final UUID deploymentId;
    private final boolean success;
    private final String errorMessage;
    
    public C2SDeploymentStatusPacket(UUID deploymentId, boolean success, String errorMessage) {
        this.deploymentId = deploymentId;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    public C2SDeploymentStatusPacket(Buffer buffer) {
        this.deploymentId = UUID.fromString(buffer.getString(1, buffer.length()));
        this.success = buffer.getByte(2) == 0;
        this.errorMessage = buffer.getString(3, buffer.length());
    }
    
    @Override
    public void serialize(Buffer buffer) {
        buffer.appendString(deploymentId.toString());
        buffer.appendByte(success ? (byte) 0 : (byte) 1);
        buffer.appendString(errorMessage);
    }
}
