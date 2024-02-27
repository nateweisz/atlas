package me.nateweisz.protocol.clientbound;

import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;

public class S2CAuthenticationStatusPacket implements Packet {

    private final boolean success;
    private final String message;

    public S2CAuthenticationStatusPacket(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public S2CAuthenticationStatusPacket(WrappedBuffer buffer) {
        this.success = buffer.nextByte() == 0;
        this.message = buffer.nextString();
    }

    @Override
    public void serialize(WrappedBuffer buffer) {
        buffer.writeByte(success ? (byte) 0 : (byte) 1); // 0 = true, 1 = false
        buffer.writeString(message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
