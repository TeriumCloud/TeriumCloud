package cloud.terium.networking.packets.service;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceOnlinePlayersUpdatePacket extends Packet {

    private final String minecraftService;
    private final int players;

    public PacketPlayOutServiceOnlinePlayersUpdatePacket(String minecraftService, int players) {
        this.minecraftService = minecraftService;
        this.players = players;
    }

    public PacketPlayOutServiceOnlinePlayersUpdatePacket(ByteBuf message) {
        this.minecraftService = readString(message);
        this.players = message.readInt();
    }

    public String minecraftService() {
        return minecraftService;
    }

    public int players() {
        return players;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(minecraftService, byteBuf);
        byteBuf.writeInt(players);
    }
}