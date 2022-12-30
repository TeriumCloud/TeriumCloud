package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public record PacketPlayOutCreateProxyGroup(String name, String groupTitle, INode node, List<INode> fallbackNodes, List<ITemplate> templates,
                                            String version, boolean maintenance, boolean isStatic, int port, int maximumPlayers,
                                            int memory, int minimalServices, int maximalServices) implements Packet {
}