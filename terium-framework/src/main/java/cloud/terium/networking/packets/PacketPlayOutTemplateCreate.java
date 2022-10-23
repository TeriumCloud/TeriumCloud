package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutTemplateCreate extends Packet {

    private final String name;
    private final String node;

    public PacketPlayOutTemplateCreate(String name, String node) {
        this.name = name;
        this.node = node;
    }

    public PacketPlayOutTemplateCreate(ByteBuf byteBuf) {
        this.name = readString(byteBuf);
        this.node = readString(byteBuf);
    }

    public String servicename() {
        return name;
    }

    public String node() {
        return node;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(node, byteBuf);
    }
}