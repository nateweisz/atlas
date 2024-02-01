package me.nateweisz.protocol;

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
}
