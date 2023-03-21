package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.event.Event;

public class ReloadGroupsEvent extends Event {

    public ReloadGroupsEvent() {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
    }
}