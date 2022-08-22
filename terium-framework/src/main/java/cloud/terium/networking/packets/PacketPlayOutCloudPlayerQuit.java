package cloud.terium.networking.packets;

import cloud.terium.networking.packet.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PacketPlayOutCloudPlayerQuit extends Packet {

    private final String name;
    private final UUID uniqueId;

    public PacketPlayOutCloudPlayerQuit(String name, UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    public PacketPlayOutCloudPlayerQuit(ByteBuf message) {
        this.name = readString(message);
        this.uniqueId = UUID.fromString(readString(message));
    }

    public String name() {
        return name;
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(uniqueId.toString(), byteBuf);
    }
}
