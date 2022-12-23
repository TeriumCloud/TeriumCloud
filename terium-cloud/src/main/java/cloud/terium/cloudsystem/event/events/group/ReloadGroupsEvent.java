package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.event.Event;

public class ReloadGroupsEvent extends Event {

    public ReloadGroupsEvent() {
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupsReload());
    }
}