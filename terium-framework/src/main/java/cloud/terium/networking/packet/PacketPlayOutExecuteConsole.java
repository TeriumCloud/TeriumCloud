package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutExecuteConsole(String command) implements Packet {
}