package cloud.terium.networking.packets.group;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutGroupDelete extends Packet {

    private final String serviceGroup;

    public PacketPlayOutGroupDelete(String serviceName) {
        this.serviceGroup = serviceName;
    }

    public PacketPlayOutGroupDelete(ByteBuf byteBuf) {
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