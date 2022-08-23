package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PacketPlayOutCloudPlayerJoin extends Packet {

    private final String name;
    private final UUID uniqueId;

    public PacketPlayOutCloudPlayerJoin(String name, UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    public PacketPlayOutCloudPlayerJoin(ByteBuf message) {
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
