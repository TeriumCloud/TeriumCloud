package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.service.CloudServiceState;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceChangeState extends Packet {

    private final String minecraftService;
    private final CloudServiceState serviceState;

    public PacketPlayOutServiceChangeState(String minecraftService, CloudServiceState serviceState) {
        this.minecraftService = minecraftService;
        this.serviceState = serviceState;
    }

    public PacketPlayOutServiceChangeState(ByteBuf message) {
        this.minecraftService = readString(message);
        this.serviceState = CloudServiceState.valueOf(readString(message));
    }

    public String minecraftService() {
        return minecraftService;
    }

    public CloudServiceState serviceState() {
        return serviceState;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(minecraftService, byteBuf);
        writeString(serviceState.name(), byteBuf);
    }
}
