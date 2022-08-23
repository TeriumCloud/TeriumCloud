package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PacketPlayOutCloudPlayerConnect extends Packet {

    private final String name;
    private final UUID uniqueId;
    private final String minecraftService;

    public PacketPlayOutCloudPlayerConnect(String name, UUID uniqueId, String service) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.minecraftService = service;
    }

    public PacketPlayOutCloudPlayerConnect(ByteBuf message) {
        this.name = readString(message);
        this.uniqueId = UUID.fromString(readString(message));
        this.minecraftService = readString(message);
    }

    public String name() {
        return name;
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public String minecraftService() {
        return minecraftService;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(name, byteBuf);
        writeString(uniqueId.toString(), byteBuf);
        writeString(minecraftService, byteBuf);
    }
}
