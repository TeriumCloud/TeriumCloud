package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutTemplateDelete extends Packet {

    private final String name;

    public PacketPlayOutTemplateDelete(String servicename) {
        this.name = servicename;
    }

    public PacketPlayOutTemplateDelete(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
    }

    public String servicename() {
        return name;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
    }
}