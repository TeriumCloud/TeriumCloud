package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record PacketPlayOutCreateLobbyGroup(String name, String groupTitle, String node, List<String> fallbackNodes,
                                            List<String> templates,
                                            String version, boolean maintenance, boolean isStatic, int maximumPlayers,
                                            int memory, int minimalServices, int maximalServices) implements Packet {

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }

    public List<INode> parsedFallbackNodes() {
        List<INode> nodeList = new ArrayList<>();
        fallbackNodes.forEach(node -> {
            nodeList.add(TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node).orElseGet(null));
        });
        return nodeList;
    }

    public List<ITemplate> parsedTemplates() {
        List<ITemplate> templateList = new ArrayList<>();
        templates.forEach(template -> {
            templateList.add(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(template).orElseGet(null));
        });
        return templateList;
    }
}