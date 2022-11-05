package cloud.terium.networking.packets.service;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceShutdown extends Packet {

    private final String minecraftService;

    public PacketPlayOutServiceShutdown(String minecraftService) {
        this.minecraftService = minecraftService;
    }

    public PacketPlayOutServiceShutdown(ByteBuf message) {
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