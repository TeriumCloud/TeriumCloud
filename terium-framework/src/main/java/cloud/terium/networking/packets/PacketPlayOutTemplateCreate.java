package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutTemplateCreate extends Packet {

    private final String name;

    public PacketPlayOutTemplateCreate(String servicename) {
        this.name = servicename;
    }

    public PacketPlayOutTemplateCreate(ByteBuf byteBuf) {
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