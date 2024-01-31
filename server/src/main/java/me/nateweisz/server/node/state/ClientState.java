package me.nateweisz.server.node.state;

public class ClientState {
    
    private AuthenticationStatus authenticationStatus;
    
    public ClientState() {
        this.authenticationStatus = AuthenticationStatus.UNAUTHENTICATED;
    }
    
    public boolean isAuthenticated() {
        return authenticationStatus == AuthenticationStatus.AUTHENTICATED;
    }
    
    public void setAuthenticated() {
        authenticationStatus = AuthenticationStatus.AUTHENTICATED;
    }
}
