package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import cloud.terium.teriumapi.template.ITemplate;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record PacketPlayOutServiceStart(String serviceName, String node) implements Packet {

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getCloudServiceByName(serviceName);
    }

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }
}