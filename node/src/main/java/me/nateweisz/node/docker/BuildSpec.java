package me.nateweisz.node.docker;

public class BuildSpec {
    // Ex. for github nateweisz/atlas (this will be formatted and validated when the packet is
    // received)
    private final String path;
    private final String type;

    public BuildSpec(String path, String type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}
