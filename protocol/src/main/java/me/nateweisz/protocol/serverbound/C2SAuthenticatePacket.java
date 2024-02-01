package me.nateweisz.protocol.serverbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

public class C2SAuthenticatePacket implements Packet {
    
    private final String secret;
    
    public C2SAuthenticatePacket(String secret) {
        this.secret = secret;
    }
    
    public C2SAuthenticatePacket(Buffer buffer) {
        this.secret = buffer.getString(1, buffer.length());
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public void serialize(Buffer buffer) {
        buffer.appendString(secret);
    }
}
