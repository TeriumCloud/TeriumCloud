package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.template.ITemplate;

public record PacketPlayOutTemplateDelete(ITemplate template) implements Packet {
}