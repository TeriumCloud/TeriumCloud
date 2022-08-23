package cloud.terium.networking.packets;

import cloud.terium.networking.packet.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutGroupReload extends Packet {

    private final String serviceGroup;

    public PacketPlayOutGroupReload(String serviceName) {
        this.serviceGroup = serviceName;
    }

    public PacketPlayOutGroupReload(ByteBuf byteBuf) {
        this.serviceGroup = readString(byteBuf);
    }

    public String serviceGroup() {
        return serviceGroup;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(serviceGroup, byteBuf);
    }
}