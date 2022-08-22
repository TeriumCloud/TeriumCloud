package cloud.terium.networking.packets;

import cloud.terium.networking.packet.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceStart extends Packet {

    private final String defaultServiceGroup;

    public PacketPlayOutServiceStart(String defaultServiceGroup) {
        this.defaultServiceGroup = defaultServiceGroup;
    }

    public PacketPlayOutServiceStart(ByteBuf message) {
        this.defaultServiceGroup = readString(message);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(defaultServiceGroup, byteBuf);
    }

    public String defaultServiceGroup() {
        return defaultServiceGroup;
    }
}