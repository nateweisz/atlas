package me.nateweisz.protocol.clientbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

public class S2CAuthenticationStatusPacket implements Packet {
    
    private final boolean success;
    private final String message;
    
    public S2CAuthenticationStatusPacket(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public S2CAuthenticationStatusPacket(Buffer buffer) {
        this.success = buffer.getByte(1) == 0;
        this.message = buffer.getString(2, buffer.length());
    }
    
    @Override
    public void serialize(Buffer buffer) {
        buffer.appendByte(success ? (byte) 0 : (byte) 1); // 0 = true, 1 = false
        buffer.appendString(message);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
