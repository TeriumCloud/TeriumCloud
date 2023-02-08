package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutCreateServerGroup;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplate;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateServerGroupEvent extends Event {

    private final String name;
    private final String groupTitle;
    private final String node;
    private final List<String> fallbackNodes;
    private final List<String> templates;
    private final String version;
    private final boolean maintenance;
    private final boolean isStatic;
    private final int maximumPlayers;
    private final int memory;
    private final int minimalServices;
    private final int maximalServices;

    public CreateServerGroupEvent(String name, String groupTitle, String node, List<String> fallbackNodes, List<String> templates, String version, boolean maintenance, boolean isStatic, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.fallbackNodes = fallbackNodes;
        this.templates = templates;
        this.version = version;
        this.maintenance = maintenance;
        this.isStatic = isStatic;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCreateServerGroup(name, groupTitle, node, fallbackNodes, templates, version, maintenance, isStatic, maximumPlayers, memory, minimalServices, maximalServices));
    }
}
