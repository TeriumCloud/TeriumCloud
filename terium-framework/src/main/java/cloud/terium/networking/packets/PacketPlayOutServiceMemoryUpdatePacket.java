package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceMemoryUpdatePacket extends Packet {

    private final String minecraftService;
    private final long memory;

    public PacketPlayOutServiceMemoryUpdatePacket(String minecraftService, long memory) {
        this.minecraftService = minecraftService;
        this.memory = memory;
    }

    public PacketPlayOutServiceMemoryUpdatePacket(ByteBuf message) {
        this.minecraftService = readString(message);
        this.memory = message.readLong();
    }

    public String minecraftService() {
        return minecraftService;
    }

    public long memory() {
        return memory;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(minecraftService, byteBuf);
        byteBuf.writeLong(memory);
    }
}
