package me.nateweisz.protocol;

import java.util.Objects;

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
    public void serialize(WrappedBuffer buffer) {
        // append the int to the buffer at the position 0,
        buffer
                .writeInt(packetId)
                .writeString(stringOne)
                .writeString(stringTwo)
                .writeInt(otherInt);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestingPacket that = (TestingPacket) o;
        return packetId == that.packetId && otherInt == that.otherInt && Objects.equals(stringOne, that.stringOne) && Objects.equals(stringTwo, that.stringTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packetId, stringOne, stringTwo, otherInt);
    }
}
