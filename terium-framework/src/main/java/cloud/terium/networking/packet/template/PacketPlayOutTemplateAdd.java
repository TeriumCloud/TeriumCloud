package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutTemplateAdd(String name, String path) implements Packet {
}