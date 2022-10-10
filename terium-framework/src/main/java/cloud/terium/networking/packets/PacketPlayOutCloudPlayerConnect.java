package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PacketPlayOutCloudPlayerConnect extends Packet {

    private final UUID uniqueId;
    private final String minecraftService;

    public PacketPlayOutCloudPlayerConnect(UUID uniqueId, String service) {
        this.uniqueId = uniqueId;
        this.minecraftService = service;
    }

    public PacketPlayOutCloudPlayerConnect(ByteBuf message) {
        this.uniqueId = UUID.fromString(readString(message));
        this.minecraftService = readString(message);
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public String minecraftService() {
        return minecraftService;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(uniqueId.toString(), byteBuf);
        writeString(minecraftService, byteBuf);
    }
}