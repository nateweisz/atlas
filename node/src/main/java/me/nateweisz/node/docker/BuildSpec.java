package me.nateweisz.node.docker;

public class BuildSpec {
    // Ex. for github nateweisz/atlas (this will be formatted and validated when the packet is received)
    private final String path;
    
    public BuildSpec(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
