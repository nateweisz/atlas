package me.nateweisz.server.node;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.server.node.packet.Packet;
import me.nateweisz.server.node.packet.Protocol;
import me.nateweisz.server.node.packet.clientbound.S2CAuthenticationStatusPacket;
import me.nateweisz.server.node.packet.serverbound.C2SAuthenticatePacket;
import me.nateweisz.server.node.state.ClientState;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeWebsocket implements Handler<ServerWebSocket>  {
    
    private final Logger logger;
    private final Map<ServerWebSocket, ClientState> connections;

    public NodeWebsocket() {
        this.logger = Logger.getLogger(NodeWebsocket.class.getName());
        this.connections = new HashMap<>();
    }
    
    @Override
    public void handle(ServerWebSocket serverWebSocket) {
        // TODO: perform validation for IP incase the user wants to limit it.
        logger.log(Level.INFO, "New connection from " + serverWebSocket.remoteAddress());
        // initialize client state as unauthenticated
        connections.put(serverWebSocket, new ClientState());
        
        serverWebSocket.binaryMessageHandler(buffer -> {
            handlePacket(buffer, serverWebSocket);
        });
        
        serverWebSocket.closeHandler(voidValue -> {
            System.out.println("Connection closed");
        });
        
        serverWebSocket.exceptionHandler(throwable -> {
            System.out.println("Exception: " + throwable.getMessage());
        });
    }
    
    private void handlePacket(Buffer buffer, ServerWebSocket serverWebSocket) {
        if (buffer.length() < 1) {
            logger.log(Level.SEVERE, "Received an invalid packet.");
            serverWebSocket.close();
            return;
        }
        
        ClientState clientState = connections.get(serverWebSocket);
        
        // get the packet id
        byte id = buffer.getByte(0);
        
        // ensure client is attempting to authenticate before sending any other packets
        if (!clientState.isAuthenticated() && id != 0x00) {
            logger.log(Level.SEVERE, "Received a packet before authentication was completed.");
            serverWebSocket.close();
            return;
        }
        Packet packet = getServerBoundPacket(id, buffer);
        
        if (packet == null) {
            logger.log(Level.SEVERE, "Received an invalid packet.");
            serverWebSocket.close();
            return;
        }
        
        if (packet instanceof C2SAuthenticatePacket) {
            // TODO: perform authentication
            // TODO: create an event bus to handle packet receiving
            clientState.setAuthenticated();
            sendPacket((byte) 0x00, new S2CAuthenticationStatusPacket(true), serverWebSocket);
        }
    }
    
    private <T extends Packet> T getServerBoundPacket(byte id, Buffer buffer) {
        return getPacket(Protocol.SERVER_BOUND, id, buffer);
    }
    
    private <T extends Packet> T getClientBoundPacket(byte id, Buffer buffer) {
        return getPacket(Protocol.CLIENT_BOUND, id, buffer);
    }
    
    private void sendPacket(byte id, Packet packet, ServerWebSocket serverWebSocket) {
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(id);
        packet.serialize(buffer);
        serverWebSocket.writeBinaryMessage(buffer);
    }

    private <T extends Packet> T getPacket(Map<Byte, Class<? extends Packet>> packets, byte id, Buffer buffer) {
        try {
            return (T) packets.get(id).getConstructor(Buffer.class).newInstance(buffer);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create packet instance.", e);
            return null;
        }
    }
}
