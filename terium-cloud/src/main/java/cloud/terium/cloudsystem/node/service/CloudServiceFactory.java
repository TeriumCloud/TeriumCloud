package cloud.terium.cloudsystem.node.service;

import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.networking.packet.service.PacketPlayOutCreateService;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudServiceFactory;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CloudServiceFactory implements ICloudServiceFactory {

    private final List<ICloudServiceGroup> bindedServiceGroups = new LinkedList<>();

    @Override
    public void createService(ICloudServiceGroup serviceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceGroup.getGroupName(), NodeStartup.getNode().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), serviceGroup.getTemplates().stream().map(ITemplate::getName).toList(), new HashMap<>(), "group_only"));
    }

    @Override
    public void createService(ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceGroup.getGroupName(), NodeStartup.getNode().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), new HashMap<>(), "group_with_templates"));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory, serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), new HashMap<>(), "full"));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates, int serviceId, int maxPlayers, int memory, HashMap<String, Object> hashMap) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, serviceId, serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), maxPlayers, memory, serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), hashMap, "full"));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, NodeStartup.getNode().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), serviceGroup.getTemplates().stream().map(ITemplate::getName).toList(), new HashMap<>(), "group_with_custom_name"));
    }

    @Override
    public void createService(String serviceName, ICloudServiceGroup serviceGroup, List<ITemplate> templates) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCreateService(serviceName, NodeStartup.getNode().getServiceProvider().getFreeServiceId(serviceGroup), serviceGroup.hasPort() ? serviceGroup.getPort() : ThreadLocalRandom.current().nextInt(20000, 50000), serviceGroup.getMaxPlayers(), serviceGroup.getMemory(), serviceGroup.getGroupNode().getName(), serviceGroup.getGroupName(), templates.stream().map(ITemplate::getName).toList(), new HashMap<>(), "group_template_and_custom_name"));
    }

    @Override
    public boolean containsServiceGroup(ICloudServiceGroup serviceGroup) {
        return bindedServiceGroups.contains(serviceGroup);
    }

    @Override
    public void bindServiceGroup(ICloudServiceGroup serviceGroup) {
        this.bindedServiceGroups.add(serviceGroup);
    }

    @Override
    public void unbindServiceGroup(ICloudServiceGroup serviceGroup) {
        this.bindedServiceGroups.remove(serviceGroup);
    }
}