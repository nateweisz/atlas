package me.nateweisz.protocol;

public interface Packet {
    void serialize(WrappedBuffer buffer);
}