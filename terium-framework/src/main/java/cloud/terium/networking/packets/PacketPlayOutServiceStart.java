package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceStart extends Packet {

    private final String serviceName;

    public PacketPlayOutServiceStart(String defaultServiceGroup) {
        this.serviceName = defaultServiceGroup;
    }

    public PacketPlayOutServiceStart(ByteBuf message) {
        this.serviceName = readString(message);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(serviceName, byteBuf);
    }

    public String serviceName() {
        return serviceName;
    }
}