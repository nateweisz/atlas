package me.nateweisz.server.node;

import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.protocol.eventbus.EventDispatcher;
import me.nateweisz.protocol.eventbus.PacketListener;
import me.nateweisz.server.Server;
import me.nateweisz.server.node.expiry.HeartbeatListener;
import me.nateweisz.server.node.expiry.NodeExpiryThread;
import me.nateweisz.server.node.state.ClientState;

import java.util.HashMap;
import java.util.Map;

public class NodeManager {
    private final Map<ServerWebSocket, ClientState> connections;
    private final EventDispatcher packetEventDispatcher;

    public NodeManager(Server server, String secret) {
        NodeExpiryThread nodeExpiryThread = new NodeExpiryThread(this);
        this.connections = new HashMap<>();
        this.packetEventDispatcher = new EventDispatcher();

        server.getHttpServer().webSocketHandler(new NodeWebsocket(this, secret));
        nodeExpiryThread.start();

        registerListeners();
    }

    public void removeClient(ClientState clientState) {
        // TODO: remove client from connections and handle any cleanup / logging we need to do
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i) == clientState) {
                connections.remove(i);
            }
        }
    }

    private void registerListeners() {
        PacketListener<?>[] listeners = {new HeartbeatListener(this)};

        for (PacketListener<?> listener : listeners) {
            packetEventDispatcher.registerListener(listener);
        }
    }

    public ClientState getConnection(ServerWebSocket serverWebSocket) {
        return connections.get(serverWebSocket);
    }

    public Map<ServerWebSocket, ClientState> getConnections() {
        return connections;
    }

    public EventDispatcher getPacketEventDispatcher() {
        return packetEventDispatcher;
    }
}
