package cloud.terium.cloudsystem.common.event.events.service;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.service.PacketPlayOutServiceRestart;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ServiceRestartEvent extends Event {

    private final String cloudService;

    public ServiceRestartEvent(String cloudService) {
        this.cloudService = cloudService;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutServiceRestart(cloudService));
    }
}