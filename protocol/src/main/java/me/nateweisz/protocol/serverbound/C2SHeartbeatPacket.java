package me.nateweisz.protocol.serverbound;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

import java.time.Instant;

public class C2SHeartbeatPacket implements Packet {
    
    private final Instant timestamp;
    
    public C2SHeartbeatPacket(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public C2SHeartbeatPacket(Buffer buffer) {
        this.timestamp = Instant.ofEpochMilli(buffer.getLong(0));
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public void serialize(Buffer buffer) {
        
    }
}
