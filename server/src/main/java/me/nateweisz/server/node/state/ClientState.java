package me.nateweisz.server.node.state;

import java.util.concurrent.TimeUnit;

public class ClientState {

    private AuthenticationStatus authenticationStatus;
    private long lastHeartbeat; // this client will be terminated if this is over 30 seconds old

    public ClientState() {
        this.authenticationStatus = AuthenticationStatus.UNAUTHENTICATED;
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - lastHeartbeat > TimeUnit.SECONDS.toMillis(30);
    }

    public boolean isAuthenticated() {
        return authenticationStatus == AuthenticationStatus.AUTHENTICATED;
    }

    public void setAuthenticated() {
        authenticationStatus = AuthenticationStatus.AUTHENTICATED;
    }
}
