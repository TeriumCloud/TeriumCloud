package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.service.PacketPlayOutServiceLock;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceLockEvent extends Event {

    private final String cloudService;

    public ServiceLockEvent(String cloudService) {
        this.cloudService = cloudService;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceLock(cloudService));
    }
}