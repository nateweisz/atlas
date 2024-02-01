package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketBase;
import me.nateweisz.protocol.clientbound.S2CAuthenticationStatusPacket;
import me.nateweisz.protocol.serverbound.C2SAuthenticatePacket;
import me.nateweisz.protocol.serverbound.C2SHeartbeatPacket;

import java.util.Map;

public class Protocol {

    public static final Map<Byte, Class<? extends Packet>> SERVER_BOUND = Map.of(
            (byte) 0x00, C2SAuthenticatePacket.class,
            (byte) 0x01, C2SHeartbeatPacket.class
    );
    
    public static final Map<Byte, Class<? extends Packet>> CLIENT_BOUND = Map.of(
            (byte) 0x00, S2CAuthenticationStatusPacket.class
    );

    public static void sendPacket(byte id, Packet packet, WebSocketBase socket) {
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(id);
        packet.serialize(buffer);
        socket.writeBinaryMessage(buffer);
    }

    public static <T extends Packet> T getPacket(Map<Byte, Class<? extends Packet>> packets, byte id, Buffer buffer) {
        try {
            return (T) packets.get(id).getConstructor(Buffer.class).newInstance(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
