package me.nateweisz.server.node.eventbus;

import me.nateweisz.protocol.Packet;

/**
 * A listener for a specific packet type.
 *
 * @param <T> The packet type.
 */
public interface PacketListener<T extends Packet> {
    Class<T> getPacketType();
    void handle(T packet);
}