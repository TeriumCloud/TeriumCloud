package cloud.terium.plugin.impl.service;

import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.List;

public class ServiceFactory implements ICloudServiceFactory {

    @Override
    public void createService(ICloudServiceGroup serviceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceGroup.getGroupName(), serviceGroup, serviceGroup.getTemplates(), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), -1));
    }

    @Override
    public void createService(ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceGroup.getGroupName(), serviceGroup, serviceGroup.getTemplates(), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), -1));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceGroup, templates, maxPlayers, memory, serviceId));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceGroup, serviceGroup.getTemplates(), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), -1));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceGroup, templates, serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), -1));
    }
}
