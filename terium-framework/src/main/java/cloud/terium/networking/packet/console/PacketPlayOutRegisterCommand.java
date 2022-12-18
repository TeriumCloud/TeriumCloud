package cloud.terium.networking.packet.console;

import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutRegisterCommand(Command command) implements Packet {
}