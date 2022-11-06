package cloud.terium.bridge.impl.service;

import cloud.terium.networking.packets.service.PacketPlayOutCreateService;
import cloud.terium.networking.packets.service.PacketPlayOutServiceStart;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.concurrent.ThreadLocalRandom;

public class ServiceFactory implements ICloudServiceFactory {

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(null, iCloudServiceGroup, null, iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory(), -1, iCloudServiceGroup.getServiceType()));
    }

    @Override
    public void createService(ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(null, iCloudServiceGroup, iTemplate, iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory(), -1, iCloudServiceGroup.getServiceType()));
    }

    @Override
    public void createService(String s, ITemplate iTemplate, CloudServiceType cloudServiceType, int i, int i1, int i2) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(s, null, iTemplate, ThreadLocalRandom.current().nextInt(20000, 50000), i, i1, i2, cloudServiceType));
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(s, iCloudServiceGroup, iCloudServiceGroup.getTemplate(), iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory(), -1, iCloudServiceGroup.getServiceType()));
    }

    @Override
    public void createService(String s, ICloudServiceGroup iCloudServiceGroup, ITemplate iTemplate) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(s, iCloudServiceGroup, iTemplate, iCloudServiceGroup.hasPort() ? iCloudServiceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), iCloudServiceGroup.getMaximumPlayers(), iCloudServiceGroup.getMemory(), -1, iCloudServiceGroup.getServiceType()));

    }

    @Override
    public void startService(ICloudService iCloudService) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceStart(iCloudService.getServiceName()));
    }
}
