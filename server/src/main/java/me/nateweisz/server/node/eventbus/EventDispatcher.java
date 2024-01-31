package me.nateweisz.server.node.eventbus;

import me.nateweisz.server.node.packet.Packet;

import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Logger;

public class EventDispatcher {
    private final Logger logger;
    private final Map<Class<? extends Packet>, ArrayList<PacketListener<?>>> listeners;

    public EventDispatcher() {
        this.logger = Logger.getLogger(EventDispatcher.class.getName());
        this.listeners = Map.of();
    }

    public void dispatchEvent(Packet packet) {
        ArrayList<PacketListener<?>> listeners = listeners.get(packet.class);

        // incase there is not registered listeners
        if (listeners == null) return;

        long startTime = System.currentTimeMillis();

        for (PacketListener<?> listener : listeners) {
            listener.handle(packet);
        }

        logger.info("Dispatched " + packet.class + " in " + (System.currentTimeMillis() - startTime) + "ms.");
    }
    
    public void registerListener(PacketListener<?> listener) {
        Class<? extends Packet> packetType = listener.getPacketType();

        if (!listeners.containsKey(packetType)) {
            listeners.put(packetType, new ArrayList<PacketListener<?>>());
        }

        // add the packer listener to the registered listeners
        listeners.get(packetType).add(listener);
    }
}
