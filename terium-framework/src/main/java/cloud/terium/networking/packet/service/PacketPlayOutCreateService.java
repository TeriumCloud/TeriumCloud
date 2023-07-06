package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.ArrayList;
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
        List<ITemplate> templateList = new ArrayList<>();
        templates.forEach(template -> {
            templateList.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(template).orElseGet(null));
        });
        return templateList;
    }
}