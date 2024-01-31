package me.nateweisz.server.node.packet;

import io.vertx.core.buffer.Buffer;

public interface Packet {
    public void serialize(Buffer buffer);
}