package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;

public class WrappedBuffer {
    private final Buffer buffer;
    private int currentIndex;

    public WrappedBuffer(Buffer buffer) {
        this.buffer = buffer;
        this.currentIndex = 0;
    }

    public WrappedBuffer(Buffer buffer, int initialIndex) {
        this.buffer = buffer;
        this.currentIndex = initialIndex;
    }

    public byte nextByte() {
        byte value = buffer.getByte(currentIndex);
        currentIndex += 1; // easier to understand what we are increasing it by doing this.
        return value;
    }

    public int nextInt() {
        int value = buffer.getInt(currentIndex);
        currentIndex += 4;
        return value;
    }

    public long nextLong() {
        long value = buffer.getLong(currentIndex);
        currentIndex += 8;
        return value;
    }

    public double nextDouble() {
        double value = buffer.getDouble(currentIndex);
        currentIndex += 8;
        return value;
    }

    public float nextFloat() {
        float value = buffer.getFloat(currentIndex);
        currentIndex += 4;
        return value;
    }

    public short nextShort() {
        short value = buffer.getShort(currentIndex);
        currentIndex += 2;
        return value;
    }

    public String nextString() {
        // first we will get an int before it that will represent the length of the string
        int stringLength = nextInt();
        String value = buffer.getString(currentIndex, currentIndex + stringLength);
        currentIndex += stringLength;
        return value;
    }

    // this are made to work in a builder format to improve readability.
    public WrappedBuffer writeByte(byte value) {
        buffer.appendByte(value);
        return this;
    }

    public WrappedBuffer writeInt(int value) {
        buffer.appendInt(value);
        return this;
    }

    public WrappedBuffer writeLong(long value) {
        buffer.appendLong(value);
        return this;
    }

    public WrappedBuffer writeDouble(double value) {
        buffer.appendDouble(value);
        return this;
    }

    public WrappedBuffer writeFloat(float value) {
        buffer.appendFloat(value);
        return this;
    }

    public WrappedBuffer writeShort(short value) {
        buffer.appendShort(value);
        return this;
    }

    public WrappedBuffer writeString(String value) {
        int stringLength = value.length();
        buffer.appendInt(stringLength);
        buffer.appendString(value);
        return this;
    }
}
