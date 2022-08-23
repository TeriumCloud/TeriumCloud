package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceRemove extends Packet {

    private final String servicename;

    public PacketPlayOutServiceRemove(String servicename) {
        this.servicename = servicename;
    }

    public PacketPlayOutServiceRemove(ByteBuf servicename) {
        this.servicename = readString(servicename);
    }

    public String serviceName() {
        return servicename;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(servicename, byteBuf);
    }
}