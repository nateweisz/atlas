package me.nateweisz.server.node.eventbus;

import me.nateweisz.protocol.Packet;

import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Logger;

public class EventDispatcher {
    private final Logger logger;
    private final HashMap<Class<? extends Packet>, ArrayList<PacketListener<? extends Packet>>> listeners;

    public EventDispatcher() {
        this.logger = Logger.getLogger(EventDispatcher.class.getName());
        this.listeners = new HashMap<>();
    }

    public <T extends Packet> void dispatchEvent(Packet packet) {
        Class<? extends Packet> packetType = packet.getClass();
        ArrayList<PacketListener<?>> listeners = this.listeners.get(packet.getClass());

        // incase there is not registered listeners
        if (listeners == null) return;

        long startTime = System.currentTimeMillis();

        for (PacketListener<? extends Packet> listener : listeners) {
            // ensure it is correctly bound
            if (listener.getPacketType().isAssignableFrom(packetType)) {
                PacketListener<T> typedListener = (PacketListener) listener;
                typedListener.handle((T) packet);
            }
        }

        logger.info("Dispatched " + packet.getClass().getName() + " in " + (System.currentTimeMillis() - startTime) + "ms.");
    }
    
    public void registerListener(PacketListener<?> listener) {
        Class<? extends Packet> packetType = listener.getPacketType();

        if (!listeners.containsKey(packetType)) {
            listeners.put(packetType, new ArrayList<>());
        }

        // add the packer listener to the registered listeners
        listeners.get(packetType).add(listener);
    }
}
