package me.nateweisz.server.buffers;


import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;
import org.junit.jupiter.api.Test;

// TODO: write a test here to validate appending and reading from a buffer
public class BufferTest {
    
    @Test public void testBuffer() {
        Buffer buffer = Buffer.buffer();
        Packet packet = new TestPacket(42);
        packet.serialize(buffer);
        TestPacket deserialized = new TestPacket(buffer);
        assert deserialized.getValue() == 42;
    }
    
    static class TestPacket implements Packet {
        private final int value;
        
        public TestPacket(int value) {
            this.value = value;
        }
        
        public TestPacket(Buffer buffer) {
            this.value = buffer.getInt(1);
        }
        
        public int getValue() {
            return value;
        }

        @Override
        public void serialize(Buffer buffer) {
            buffer.appendByte((byte) 0x00);
            buffer.appendInt(value);
        }
    }
}
