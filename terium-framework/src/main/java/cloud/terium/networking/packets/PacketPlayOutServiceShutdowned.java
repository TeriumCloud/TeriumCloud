package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceShutdowned extends Packet {

    private final String minecraftService;

    public PacketPlayOutServiceShutdowned(String minecraftService) {
        this.minecraftService = minecraftService;
    }

    public PacketPlayOutServiceShutdowned(ByteBuf message) {
        this.minecraftService = readString(message);
    }

    public String minecraftService() {
        return minecraftService;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(minecraftService, byteBuf);
    }
}