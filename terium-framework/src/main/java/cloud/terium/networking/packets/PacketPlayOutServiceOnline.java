package cloud.terium.networking.packets;

import cloud.terium.networking.packet.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceOnline extends Packet {

    private final String minecraftService;
    private final boolean online;

    public PacketPlayOutServiceOnline(String minecraftService, boolean online) {
        this.minecraftService = minecraftService;
        this.online = online;
    }

    public PacketPlayOutServiceOnline(ByteBuf message) {
        this.minecraftService = readString(message);
        this.online = Boolean.parseBoolean(readString(message));
    }

    public String minecraftService() {
        return minecraftService;
    }

    public boolean online() {
        return online;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(minecraftService, byteBuf);
        writeString(online + "", byteBuf);
    }
}
