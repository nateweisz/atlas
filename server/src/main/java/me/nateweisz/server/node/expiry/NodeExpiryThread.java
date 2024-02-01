package me.nateweisz.server.node.expiry;

import me.nateweisz.server.node.NodeManager;
import me.nateweisz.server.node.state.ClientState;

public class NodeExpiryThread extends Thread {
    private final NodeManager nodeManager;
    
    public NodeExpiryThread(NodeManager nodeManager) {
        super("Node Expiry Thread");
        this.nodeManager = nodeManager;
    }
    
    @Override
    public void run() {
        while (true) {
            for (ClientState clientState : nodeManager.getConnections().values()) {
                if (clientState.isExpired()) {
                    nodeManager.removeClient(clientState);
                }
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
