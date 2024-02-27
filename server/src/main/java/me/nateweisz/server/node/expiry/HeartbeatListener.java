package me.nateweisz.server.node.expiry;

import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.protocol.eventbus.PacketListener;
import me.nateweisz.protocol.serverbound.C2SHeartbeatPacket;
import me.nateweisz.server.node.NodeManager;
import me.nateweisz.server.node.state.ClientState;

public class HeartbeatListener implements PacketListener<C2SHeartbeatPacket> {
    private final NodeManager nodeManager;

    public HeartbeatListener(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    public Class<C2SHeartbeatPacket> getPacketType() {
        return C2SHeartbeatPacket.class;
    }

    public void handle(C2SHeartbeatPacket packet, ServerWebSocket serverWebSocket) {
        ClientState state = nodeManager.getConnection(serverWebSocket);
        state.setLastHeartbeat(packet.getTimestamp().toEpochMilli());

    }
}