package me.nateweisz.server.buffers;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;
import org.junit.jupiter.api.Test;

public class BufferStringTest {
    
    @Test public void testStringDeserialization() {
        Buffer buffer = Buffer.buffer();
        Packet packet = new TestingPacket(1, "TESTING STRING ONE", "STRING TWO TEST", 5);
        packet.serialize(buffer);
        TestingPacket deserialized = new TestingPacket(new WrappedBuffer(buffer));
        
        System.out.printf("%s, %s, %s, %s", deserialized.getPacketId(), deserialized.getStringOne(), deserialized.getStringTwo(), deserialized.getOtherInt());
        
        assert deserialized.getPacketId() == 1;
        assert deserialized.getStringOne().equals("TESTING STRING ONE");
        assert deserialized.getStringTwo().equals("STRING TWO TEST");
        assert deserialized.getOtherInt() == 5;
    }
    
    
}
