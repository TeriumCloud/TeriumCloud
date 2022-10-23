package cloud.terium.bridge.impl.service;

import cloud.terium.networking.packets.PacketPlayOutCreateService;
import cloud.terium.networking.packets.PacketPlayOutServiceStart;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

public class ServiceFactory implements ICloudServiceFactory {

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(null, iCloudServiceGroup, null, iCloudServiceGroup.getPort(), iCloudServiceGroup.getMaximumPlayers()));
    }

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(null, iCloudServiceGroup, iTemplate, iCloudServiceGroup.getPort(), iCloudServiceGroup.getMaximumPlayers()));
    }

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate, int i, int i1) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(null, iCloudServiceGroup, iTemplate, i, i1));
    }

    @Override
    public void createService(String s, ITemplate iTemplate) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(s, null, iTemplate, -1, -1));
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(s, iCloudServiceGroup, null, iCloudServiceGroup.getPort(), iCloudServiceGroup.getMaximumPlayers()));
    }

    @Override
    public void startService(ICloudService iCloudService) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceStart(iCloudService.getServiceGroup().getServiceGroupName()));
    }
}
