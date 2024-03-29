package cloud.terium.networking.packet.console;

import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutSendConsole(String message, LogType logType) implements Packet {
}