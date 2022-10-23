package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutExecuteConsole extends Packet {

    private final String command;

    public PacketPlayOutExecuteConsole(String command) {
        this.command = command;
    }

    public PacketPlayOutExecuteConsole(ByteBuf byteBuf) {
        this.command = readString(byteBuf);
    }

    public String command() {
        return command;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(command, byteBuf);
    }
}
