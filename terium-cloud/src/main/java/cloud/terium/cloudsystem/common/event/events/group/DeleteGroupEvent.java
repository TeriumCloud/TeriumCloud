package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.group.PacketPlayOutGroupDelete;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class DeleteGroupEvent extends Event {

    private final String cloudServiceGroup;

    public DeleteGroupEvent(String cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupDelete(cloudServiceGroup));
    }
}