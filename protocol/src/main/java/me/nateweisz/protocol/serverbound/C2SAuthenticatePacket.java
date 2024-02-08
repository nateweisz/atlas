package me.nateweisz.protocol.serverbound;

import me.nateweisz.protocol.WrappedBuffer;
import me.nateweisz.protocol.Packet;

public class C2SAuthenticatePacket implements Packet {
    
    private final String secret;
    
    public C2SAuthenticatePacket(String secret) {
        this.secret = secret;
    }
    
    public C2SAuthenticatePacket(WrappedBuffer buffer) {
        this.secret = buffer.nextString();
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public void serialize(WrappedBuffer buffer) {
        buffer.writeString(secret);
    }
}
