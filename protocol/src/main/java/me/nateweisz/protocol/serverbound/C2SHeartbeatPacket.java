package me.nateweisz.protocol.serverbound;

import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;

import java.time.Instant;

public class C2SHeartbeatPacket implements Packet {

    private final Instant timestamp;

    public C2SHeartbeatPacket(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public C2SHeartbeatPacket(WrappedBuffer buffer) {
        this.timestamp = Instant.ofEpochMilli(buffer.nextLong());
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public void serialize(WrappedBuffer buffer) {
        buffer.writeLong(timestamp.toEpochMilli());
    }
}
