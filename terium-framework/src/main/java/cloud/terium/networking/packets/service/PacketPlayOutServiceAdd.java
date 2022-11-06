package cloud.terium.networking.packets.service;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceAdd extends Packet {

    private final String serviceName;
    private final String serviceGroup;
    private final String template;
    private final int serviceId;
    private final int port;

    public PacketPlayOutServiceAdd(String serviceName, String serviceGroup, String template, int serviceId, int port) {
        this.serviceName = serviceName;
        this.serviceGroup = serviceGroup;
        this.template = template;
        this.serviceId = serviceId;
        this.port = port;
    }

    public PacketPlayOutServiceAdd(ByteBuf byteBuf) {
        this.serviceName = readString(byteBuf);
        this.serviceGroup = readString(byteBuf);
        this.template = readString(byteBuf);
        this.serviceId = byteBuf.readInt();
        this.port = byteBuf.readInt();
    }

    public String serviceName() {
        return serviceName;
    }

    public String serviceGroup() {
        return serviceGroup;
    }

    public String template() {
        return template;
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
        writeString(template, byteBuf);
        byteBuf.writeInt(serviceId);
        byteBuf.writeInt(port);
    }
}