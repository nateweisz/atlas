package me.nateweisz.protocol.eventbus;

import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.protocol.Packet;

/**
 * A listener for a specific packet type.
 *
 * @param <T> The packet type.
 */
public interface PacketListener<T extends Packet> {
    Class<T> getPacketType();

    // listeners on the node should never touch the serverWebSocket since it will be passed in as
    // null there.
    void handle(T packet, ServerWebSocket serverWebSocket);
}
