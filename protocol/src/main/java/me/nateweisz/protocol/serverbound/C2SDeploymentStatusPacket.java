package me.nateweisz.protocol.serverbound;

import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;

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

    public C2SDeploymentStatusPacket(WrappedBuffer buffer) {
        this.deploymentId = UUID.fromString(buffer.nextString());
        this.success = buffer.nextByte() == 0;
        this.errorMessage = buffer.nextString();
    }

    @Override
    public void serialize(WrappedBuffer buffer) {
        buffer.writeString(deploymentId.toString());
        buffer.writeByte(success ? (byte) 0 : (byte) 1);
        buffer.writeString(errorMessage);
    }
}
