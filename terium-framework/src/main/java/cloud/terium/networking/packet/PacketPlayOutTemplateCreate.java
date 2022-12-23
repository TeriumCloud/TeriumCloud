package cloud.terium.networking.packet;

import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutTemplateCreate(String name) implements Packet {
}