package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocketBase;
import me.nateweisz.protocol.clientbound.S2CAuthenticationStatusPacket;
import me.nateweisz.protocol.clientbound.S2CRequestDeploymentPacket;
import me.nateweisz.protocol.serverbound.C2SAuthenticatePacket;
import me.nateweisz.protocol.serverbound.C2SDeploymentStatusPacket;
import me.nateweisz.protocol.serverbound.C2SHeartbeatPacket;

import java.util.Map;

public class Protocol {

    public static final Map<Byte, Class<? extends Packet>> SERVER_BOUND = Map.of(
            (byte) 0x00, C2SAuthenticatePacket.class,
            (byte) 0x01, C2SHeartbeatPacket.class,
            (byte) 0x02, C2SDeploymentStatusPacket.class
    );

    public static final Map<Byte, Class<? extends Packet>> CLIENT_BOUND = Map.of(
            (byte) 0x00, S2CAuthenticationStatusPacket.class,
            (byte) 0x01, S2CRequestDeploymentPacket.class
    );

    public static void sendPacket(byte id, Packet packet, WebSocketBase socket) {
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(id);
        packet.serialize(new WrappedBuffer(buffer));
        socket.writeBinaryMessage(buffer);
    }

    public static <T extends Packet> T getPacket(Map<Byte, Class<? extends Packet>> packets, byte id, Buffer buffer) {
        buffer = buffer.getBuffer(1, buffer.length()); // get a new buffer without the packet id in it.         
        try {
            return (T) packets.get(id).getConstructor(WrappedBuffer.class).newInstance(new WrappedBuffer(buffer));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
