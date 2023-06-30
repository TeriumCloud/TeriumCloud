package cloud.terium.networking.packet.service;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ICloudService;

import java.util.Optional;

public record PacketPlayOutSuccessfullyServiceStarted(String serviceName, String node) implements Packet {

    public Optional<ICloudService> parsedCloudService() {
        return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(serviceName);
    }

    public Optional<INode> parsedNode() {
        return TeriumAPI.getTeriumAPI().getProvider().getNodeProvider().getNodeByName(node);
    }
}