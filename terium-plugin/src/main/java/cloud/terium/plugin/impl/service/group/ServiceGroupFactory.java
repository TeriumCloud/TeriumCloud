package cloud.terium.plugin.impl.service.group;

import cloud.terium.networking.packet.group.PacketPlayOutCreateLobbyGroup;
import cloud.terium.networking.packet.group.PacketPlayOutCreateProxyGroup;
import cloud.terium.networking.packet.group.PacketPlayOutCreateServerGroup;
import cloud.terium.networking.packet.group.PacketPlayOutGroupDelete;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.service.group.impl.DefaultLobbyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultProxyGroup;
import cloud.terium.teriumapi.service.group.impl.DefaultServerGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public class ServiceGroupFactory implements ICloudServiceGroupFactory {

    @Override
    public ICloudServiceGroup createLobbyGroup(String name, String groupTitle, INode node, List<INode> fallbackNodes, List<ITemplate> templates, String version, boolean maintenance, boolean isStatic, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateLobbyGroup(name, groupTitle, node.getName(), fallbackNodes.stream().map(INode::getName).toList(), templates.stream().map(ITemplate::getName).toList(), version, maintenance, isStatic, maximumPlayers, memory, minimalServices, maximalServices));
        return new DefaultLobbyGroup(name, groupTitle, node, fallbackNodes, templates, version, maintenance, isStatic, maximumPlayers, memory, minimalServices, maximalServices);
    }

    @Override
    public ICloudServiceGroup createProxyGroup(String name, String groupTitle, INode node, List<INode> fallbackNodes, List<ITemplate> templates, String version, boolean maintenance, boolean isStatic, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateProxyGroup(name, groupTitle, node.getName(), fallbackNodes.stream().map(INode::getName).toList(), templates.stream().map(ITemplate::getName).toList(), version, maintenance, isStatic, port, maximumPlayers, memory, minimalServices, maximalServices));
        return new DefaultProxyGroup(name, groupTitle, node, fallbackNodes, templates, version, maintenance, isStatic, port, maximumPlayers, memory, minimalServices, maximalServices);
    }

    @Override
    public ICloudServiceGroup createServerGroup(String name, String groupTitle, INode node, List<INode> fallbackNodes, List<ITemplate> templates, String version, boolean maintenance, boolean isStatic, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateServerGroup(name, groupTitle, node.getName(), fallbackNodes.stream().map(INode::getName).toList(), templates.stream().map(ITemplate::getName).toList(), version, maintenance, isStatic, maximumPlayers, memory, minimalServices, maximalServices));
        return new DefaultServerGroup(name, groupTitle, node, fallbackNodes, templates, version, maintenance, isStatic, maximumPlayers, memory, minimalServices, maximalServices);
    }

    @Override
    public void deleteServiceGroup(ICloudServiceGroup cloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupDelete(cloudServiceGroup.getGroupName()));
    }
}
