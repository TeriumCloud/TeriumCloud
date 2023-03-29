package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record PacketPlayOutCreateService(String serviceName, int serviceId, int port, int maxPlayers, int memory,
                                         String node, String serviceGroup, List<String> templates,
                                         HashMap<String, Object> propertyCache, String type) implements Packet {

    public Optional<ICloudServiceGroup> parsedServiceGroup() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(serviceGroup);
    }

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }

    public List<ITemplate> parsedTemplates() {
        return TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates();
    }
}