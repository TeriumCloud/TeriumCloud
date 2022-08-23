package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceForceShutdown extends Packet {

    private final String serviceName;

    public PacketPlayOutServiceForceShutdown(String serviceName) {
        this.serviceName = serviceName;
    }

    public PacketPlayOutServiceForceShutdown(ByteBuf byteBuf) {
        this.serviceName = readString(byteBuf);
    }

    public String serviceName() {
        return serviceName;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(serviceName, byteBuf);
    }
}
