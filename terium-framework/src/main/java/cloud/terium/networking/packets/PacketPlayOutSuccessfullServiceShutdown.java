package cloud.terium.networking.packets;

import cloud.terium.networking.packet.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutSuccessfullServiceShutdown extends Packet {

    private final String servicename;

    public PacketPlayOutSuccessfullServiceShutdown(String servicename) {
        this.servicename = servicename;
    }

    public PacketPlayOutSuccessfullServiceShutdown(ByteBuf byteBuf) {
        this.servicename = readString(byteBuf);
    }

    public String servicename() {
        return servicename;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(servicename, byteBuf);
    }
}
