package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;

public class TestingPacket implements Packet {
    private final int packetId; // this is only present here for simulation usage in unit tests
    private final String stringOne;
    private final String stringTwo;
    private final int otherInt;
    
    public TestingPacket(int packetId, String stringOne, String stringTwo, int otherInt) {
        this.packetId = packetId;
        this.stringOne = stringOne;
        this.stringTwo = stringTwo;
        this.otherInt = otherInt;
    }
    
    public TestingPacket(WrappedBuffer wrappedBuffer) {
        packetId = wrappedBuffer.nextInt();
        stringOne = wrappedBuffer.nextString();
        stringTwo = wrappedBuffer.nextString();
        otherInt = wrappedBuffer.nextInt();
    }

    @Override
    public void serialize(Buffer buffer) {
        // append the int to the buffer at the position 0,
        buffer.appendInt(packetId);
        buffer.appendInt(stringOne.length());
        buffer.appendString(stringOne);
        buffer.appendInt(stringTwo.length());
        buffer.appendInt(otherInt);
    }

    public boolean equals(TestingPacket packet) {
        return packet.getPacketId() == packetId && packet.getStringOne().equals(stringOne) && packet.getStringTwo().equals(stringTwo) && packet.getOtherInt() == otherInt;
    }

    public int getPacketId() {
        return packetId;
    }

    public String getStringOne() {
        return stringOne;
    }

    public String getStringTwo() {
        return stringTwo;
    }

    public int getOtherInt() {
        return otherInt;
    }
}
