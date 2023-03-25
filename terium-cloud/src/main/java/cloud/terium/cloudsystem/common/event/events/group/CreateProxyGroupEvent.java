package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.group.PacketPlayOutCreateProxyGroup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateProxyGroupEvent extends Event {

    private final String name;
    private final String groupTitle;
    private final String node;
    private final List<String> fallbackNodes;
    private final List<String> templates;
    private final String version;
    private final boolean maintenance;
    private final boolean isStatic;
    private final int port;
    private final int maximumPlayers;
    private final int memory;
    private final int minimalServices;
    private final int maximalServices;

    public CreateProxyGroupEvent(String name, String groupTitle, String node, List<String> fallbackNodes, List<String> templates, String version, boolean maintenance, boolean isStatic, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.fallbackNodes = fallbackNodes;
        this.templates = templates;
        this.version = version;
        this.maintenance = maintenance;
        this.isStatic = isStatic;
        this.port = port;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateProxyGroup(name, groupTitle, node, fallbackNodes, templates, version, maintenance, isStatic, port, maximumPlayers, memory, minimalServices, maximalServices));
    }
}
