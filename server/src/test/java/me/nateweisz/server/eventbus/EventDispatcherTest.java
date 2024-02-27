package me.nateweisz.server.eventbus;

import io.vertx.core.http.ServerWebSocket;
import me.nateweisz.protocol.Packet;
import me.nateweisz.protocol.WrappedBuffer;
import me.nateweisz.protocol.eventbus.EventDispatcher;
import me.nateweisz.protocol.eventbus.PacketListener;
import org.junit.jupiter.api.Test;

public class EventDispatcherTest {

    @Test
    public void testEventBus() {
        EventDispatcher ed = new EventDispatcher();
        ed.registerListener(new TestListener(-1));

        TestPacket packet = new TestPacket(0);
        ed.dispatchEvent(packet, null);
        // at this point the value of TestPacket should be -1 since there is only one listener
        assert packet.getValue() == -1;

        ed.registerListener(new TestListener(100));
        ed.dispatchEvent(packet, null);
        // the value of TestPacket should now be 100 since that is the last listener of that type to be
        // invoked.
        assert packet.getValue() == 100;
    }

    static class TestListener implements PacketListener<TestPacket> {
        private final int value;

        public TestListener(int value) {
            this.value = value;
        }

        public Class<TestPacket> getPacketType() {
            return TestPacket.class;
        }

        @Override
        public void handle(TestPacket packet, ServerWebSocket serverWebSocket) {
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
        public void serialize(WrappedBuffer buffer) {
            buffer.writeByte((byte) 0x00);
            buffer.writeInt(value);
        }
    }
}