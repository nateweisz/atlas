package me.nateweisz.server.buffers;

import io.vertx.core.buffer.Buffer;
import me.nateweisz.protocol.Packet;
import org.junit.jupiter.api.Test;

public class BufferStringTest {
    
    @Test public void testStringDeserialization() {
        Buffer buffer = Buffer.buffer();
        Packet packet = new TestPacket();
        packet.serialize(buffer);
        TestPacket deserialized = new TestPacket(buffer);
        System.out.println("Value: " + deserialized.getValue());
        System.out.println("Test: " + deserialized.getTest());
        assert deserialized.getValue().equals("test");
        assert deserialized.getTest().equals("a");
    }

    static class TestPacket implements Packet {
        private final String value;
        private final String test;

        public TestPacket() {
            this.value = "test"; // length 4
            this.test = "a"; // length 1
        }

        public TestPacket(Buffer buffer) {
            this.value = buffer.getString(0, 4);
            this.test = buffer.getString(4, 5);
        }

        public String getValue() {
            return value;
        }

        public String getTest() {
            return test;
        }

        @Override
        public void serialize(Buffer buffer) {
            buffer.appendString(value);
            buffer.appendString(test);
        }
    }
}
