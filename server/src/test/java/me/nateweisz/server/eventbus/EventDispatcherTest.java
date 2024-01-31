package me.nateweisz.server.eventbus;

import me.nateweisz.server.node.eventbus.*;
import me.nateweisz.server.node.packet.Packet;
import org.junit.jupiter.api.Test;

public class EventDispatcherTest {

    @Test public void testEventBus() {
        EventDispatcher ed = new EventDispatcher();
        ed.registerListener(new TestListener(-1));

        Packet packet = new TestPacket(0);
        ed.dispatchEvent(packet);
        // at this point the value of TestPacket should be -1 since there is only one listener
        assert packet.getValue() == -1;

        ed.registerListener(new TestListener(100));
        ed.dispatchEvent(packet);
        // the value of TestPacket should now be 100 since that is the last listener of that type to be
        // invoked.
        assert packet.getValue() == 100;
    }

    static class TestListener implements PacketListener<TestPacket> {
        private int value;

        public TestListener(int value) {
            this.value = value;
        }

        public Class<T> getPacketType() {
            return TestPacket.getClass();
        }

        public void handle(TestPacket packet) {
            packet.setValue(value);
        }
    }

    static class TestPacket implements Packet {
        private int value;
        
        public TestPacket(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public void serialize(Buffer buffer) {
            buffer.appendByte((byte) 0x00);
            buffer.appendInt(value);
        }
    }
}