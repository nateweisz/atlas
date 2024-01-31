package me.nateweisz.server.node.packet.serverbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.server.node.packet.Packet;

public class C2SAuthenticatePacket implements Packet {
    
    private final String secret;
    
    public C2SAuthenticatePacket(Buffer buffer) {
        this.secret = buffer.getString(0, buffer.length());
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public void serialize(Buffer buffer) {
        buffer.appendString(secret);
    }
}
