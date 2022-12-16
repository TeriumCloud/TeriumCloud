package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.template.ITemplate;

public record PacketPlayOutCreateServerGroup(String name, String groupTitle, String node, ITemplate template,
                                             String version, boolean maintenance, int maximumPlayers, int memory,
                                             int minimalServices, int maximalServices) implements Packet {
}