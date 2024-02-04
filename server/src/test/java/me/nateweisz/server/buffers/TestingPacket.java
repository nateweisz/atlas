package me.nateweisz.server.buffers;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;

public class TestingPacket implements Packet {
    // idea: put number that we should offset for fetching string before we call buffer.getString
    private final static String SPLITTER = "\u0003";
    
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
    
    public TestingPacket(Buffer buffer) {
        int currentIndex = 0;
        packetId = buffer.getInt(currentIndex);
        // int increases by 4 (I need to make a chart on this if I were to choose this solution and write abstraction for it)
        currentIndex += 4;
        
        String[] strings = buffer.getString(currentIndex, buffer.length() - 1).split(SPLITTER); // -1 because we have after the packet and dont want to have that at all
        stringOne = strings[0];
        stringTwo = strings[1];
        
        currentIndex = buffer.length() - 4;
        
        otherInt = buffer.getInt(currentIndex);
    }

    @Override
    public void serialize(Buffer buffer) {
        // append the int to the buffer at the position 0,
        buffer.appendInt(packetId);
        
        String strings = "";
        strings += stringOne + SPLITTER;
        strings += stringTwo + SPLITTER;
        
        buffer.appendString(strings);
        
        buffer.appendInt(otherInt);
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
