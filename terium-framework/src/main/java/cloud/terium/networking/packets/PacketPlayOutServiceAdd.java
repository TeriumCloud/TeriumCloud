package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceAdd extends Packet {

    private final String serviceName;
    private final String serviceGroup;
    private final int serviceId;
    private final int port;

    public PacketPlayOutServiceAdd(String serviceName, String serviceGroup, int serviceId, int port) {
        this.serviceName = serviceName;
        this.serviceGroup = serviceGroup;
        this.serviceId = serviceId;
        this.port = port;
    }

    public PacketPlayOutServiceAdd(ByteBuf byteBuf) {
        this.serviceName = readString(byteBuf);
        this.serviceGroup = readString(byteBuf);
        this.serviceId = byteBuf.readInt();
        this.port = byteBuf.readInt();
    }

    public String serviceName() {
        return serviceName;
    }

    public String serviceGroup() {
        return serviceGroup;
    }

    public int serviceId() {
        return serviceId;
    }

    public int port() {
        return port;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(serviceName, byteBuf);
        writeString(serviceGroup, byteBuf);
        byteBuf.writeInt(serviceId);
        byteBuf.writeInt(port);
    }
}