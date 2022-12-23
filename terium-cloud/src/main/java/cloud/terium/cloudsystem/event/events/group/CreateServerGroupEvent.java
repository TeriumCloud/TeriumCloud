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
    private final INode node;
    private final List<ITemplate> templates;
    private final String version;
    private final boolean maintenance;
    private final int maximumPlayers;
    private final int memory;
    private final int minimalServices;
    private final int maximalServices;

    public CreateServerGroupEvent(String name, String groupTitle, INode node, List<ITemplate> templates, String version, boolean maintenance, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        this.name = name;
        this.groupTitle = groupTitle;
        this.node = node;
        this.templates = templates;
        this.version = version;
        this.maintenance = maintenance;
        this.maximumPlayers = maximumPlayers;
        this.memory = memory;
        this.minimalServices = minimalServices;
        this.maximalServices = maximalServices;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutCreateServerGroup(name, groupTitle, node, templates, version, maintenance, maximumPlayers, memory, minimalServices, maximalServices));
    }
}
