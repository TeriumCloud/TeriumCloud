package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutCopyServiceToTemplate(String service, String template) implements Packet {
}