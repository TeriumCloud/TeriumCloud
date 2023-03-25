package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Event;

public class ReloadGroupsEvent extends Event {

    public ReloadGroupsEvent() {
        if (ClusterStartup.getCluster() != null)
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
    }
}