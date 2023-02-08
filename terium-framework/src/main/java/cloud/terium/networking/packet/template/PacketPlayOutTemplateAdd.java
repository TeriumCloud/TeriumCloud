package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutTemplateAdd(String name, String path) implements Packet {
}