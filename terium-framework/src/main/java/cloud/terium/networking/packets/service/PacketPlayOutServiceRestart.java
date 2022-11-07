package cloud.terium.networking.packets.service;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutServiceRestart extends Packet {

    private final String servicename;

    public PacketPlayOutServiceRestart(String servicename) {
        this.servicename = servicename;
    }

    public PacketPlayOutServiceRestart(ByteBuf servicename) {
        this.servicename = readString(servicename);
    }

    public String serviceName() {
        return servicename;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(servicename, byteBuf);
    }
}