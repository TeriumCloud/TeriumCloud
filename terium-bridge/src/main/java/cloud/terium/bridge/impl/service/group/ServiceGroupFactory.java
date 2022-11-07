package cloud.terium.bridge.impl.service.group;

import cloud.terium.networking.packets.group.*;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.service.group.ICloudServiceGroupFactory;
import cloud.terium.teriumapi.template.ITemplate;

public class ServiceGroupFactory implements ICloudServiceGroupFactory {

    @Override
    public void createLobbyGroup(String name, String groupTitle, String node, ITemplate template, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateLobbyGroup(name, groupTitle, node, template, version, true, maximumPlayers, memory, minimalServices, maximalServices));
    }

    @Override
    public void createServerGroup(String name, String groupTitle, String node, ITemplate template, String version, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateServerGroup(name, groupTitle, node, template, version, true, maximumPlayers, memory, minimalServices, maximalServices));
    }

    @Override
    public void createProxyGroup(String name, String groupTitle, String node, ITemplate template, String version, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateProxyGroup(name, groupTitle, node, template, version, true, port, maximumPlayers, memory, minimalServices, maximalServices));
    }

    @Override
    public void deleteServiceGroup(ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupDelete(iCloudServiceGroup.getServiceGroupName()));
    }
}