package cloud.terium.networking.packets;

import cloud.terium.networking.packet.Packet;
import cloud.terium.teriumapi.console.LogType;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutSendConsole extends Packet {

    private final String message;
    private final LogType logType;

    public PacketPlayOutSendConsole(String message, LogType logType) {
        this.message = message;
        this.logType = logType;
    }

    public PacketPlayOutSendConsole(ByteBuf byteBuf) {
        this.message = readString(byteBuf);
        this.logType = LogType.valueOf(readString(byteBuf));
    }

    public String message() {
        return message;
    }

    public LogType logType() {
        return logType;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(message, byteBuf);
        writeString(logType.name(), byteBuf);
    }
}
