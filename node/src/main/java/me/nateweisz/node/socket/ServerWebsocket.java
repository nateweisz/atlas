package me.nateweisz.node.socket;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocket;
import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.Protocol;
import me.nateweisz.protocol.clientbound.S2CAuthenticationStatusPacket;
import me.nateweisz.protocol.eventbus.EventDispatcher;
import me.nateweisz.protocol.serverbound.C2SAuthenticatePacket;

import java.util.Map;
import java.util.logging.Level;

public class ServerWebsocket implements Handler<AsyncResult<WebSocket>> {
    
    private final String secret;
    private final EventDispatcher eventDispatcher;
    
    public ServerWebsocket(String secret) {
        this.secret = secret;
        this.eventDispatcher = new EventDispatcher();
    }
    
    @Override
    public void handle(AsyncResult<WebSocket> webSocketAsyncResult) {
        if (webSocketAsyncResult.failed()) {
            System.out.println("Failed to connect to server");
            return;
        }
        
        WebSocket webSocket = webSocketAsyncResult.result();
        
        // First we need to write the authentication packet
        Protocol.sendPacket((byte) 0x00, new C2SAuthenticatePacket(secret), webSocket);
        
        webSocket.binaryMessageHandler((this::handlePacket));
        
        webSocket.closeHandler((test) -> {
            System.out.println("Websocket connection has been closed.");
        });
    }
    
    private void handlePacket(Buffer buffer) {
        byte id = buffer.getByte(0);
        Packet packet = getClientBoundPacket(id, buffer);
        
        if (packet == null) {
            System.out.println("Received an invalid packet.");
            return;
        }
        
        if (packet instanceof S2CAuthenticationStatusPacket statusPacket) {
            if (statusPacket.isSuccess()) {
                System.out.println("Successfully authenticated with server.");
            } else {
                System.out.println("Failed to authenticate with server: " + statusPacket.getMessage());
                return;
            }
        }
        
        System.out.println("Received packet: " + packet.getClass().getSimpleName());
        eventDispatcher.dispatchEvent(packet, null);
    }

    // TODO: write abstraction for the websocket binary data stuff cause copy paste bad!!

    private <T extends Packet> T getClientBoundPacket(byte id, Buffer buffer) {
        return Protocol.getPacket(Protocol.CLIENT_BOUND, id, buffer);
    }
}
