package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;

public interface Packet {
    void serialize(Buffer buffer);
}