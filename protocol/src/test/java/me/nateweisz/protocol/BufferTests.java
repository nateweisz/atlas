package me.nateweisz.protocol;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.Test;

public class BufferTests {
    @Test
    public void testStringDeserialization() {
        Buffer buffer = Buffer.buffer();
        Packet packet = new TestingPacket(1, "TESTING STRING ONE", "STRING TWO TEST", 5);
        packet.serialize(new WrappedBuffer(buffer));
        TestingPacket deserialized = new TestingPacket(new WrappedBuffer(buffer));

        System.out.printf("%s, %s, %s, %s", deserialized.getPacketId(), deserialized.getStringOne(), deserialized.getStringTwo(), deserialized.getOtherInt());

        assert deserialized.equals(packet);
    }
}