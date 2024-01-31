package me.nateweisz.server.node.eventbus;

import me.nateweisz.server.node.packet.Packet;

import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Map;

public class EventDispatcher {
    private final Map<? extends Packet, List<PacketListener<?>>> listeners;

    public EventDispatcher() {
        this.listeners = Map.of();
    }
    
    public void registerListener(PacketListener<?> listener) {
        Class<? extends Packet> packetType = listener.getPacketType();
        List<PacketListener<?>> packetListeners = listeners.get(packetType); // todo add a check for packet's ot already here
        packetListeners.add(listener);
    }
}
