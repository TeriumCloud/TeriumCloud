package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutServiceExecuteCommand(String cloudService, String command) implements Packet {
}