package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.service.CloudServiceState;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutUpdateService extends Packet {

    private final String servicename;
    private final boolean locked;
    private final CloudServiceState serviceState;
    private final int onlinePlayers;
    private final long usedMemory;

    public PacketPlayOutUpdateService(String servicename, boolean locked, CloudServiceState serviceState, int onlinePlayers, long usedMemory) {
        this.servicename = servicename;
        this.locked = locked;
        this.serviceState = serviceState;
        this.onlinePlayers = onlinePlayers;
        this.usedMemory = usedMemory;
    }

    public PacketPlayOutUpdateService(ByteBuf byteBuf) {
        this.servicename = readString(byteBuf);
        this.locked = Boolean.parseBoolean(readString(byteBuf));
        this.serviceState = CloudServiceState.valueOf(readString(byteBuf));
        this.onlinePlayers = byteBuf.readInt();
        this.usedMemory = byteBuf.readLong();
    }

    public String servicename() {
        return servicename;
    }

    public boolean locked() {
        return locked;
    }

    public CloudServiceState serviceState() {
        return serviceState;
    }

    public int onlinePlayers() {
        return onlinePlayers;
    }

    public long usedMemory() {
        return usedMemory;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(servicename, byteBuf);
        writeString(locked + "", byteBuf);
        writeString(serviceState.name(), byteBuf);
        byteBuf.writeInt(onlinePlayers);
        byteBuf.writeLong(usedMemory);
    }
}