package cloud.terium.networking.packet.group;

import cloud.terium.teriumapi.pipe.Packet;
import cloud.terium.teriumapi.service.ServiceType;

import java.util.List;

public record PacketPlayOutGroupAdd(String name, String groupTitle, String node, List<String> fallbackNodes,
                                    List<String> templates, ServiceType serviceType, String version,
                                    boolean maintenance, boolean isStatic, boolean hasPort, int port,
                                    int maximumPlayers, int memory, int minimalServices,
                                    int maximalServices) implements Packet {
}
