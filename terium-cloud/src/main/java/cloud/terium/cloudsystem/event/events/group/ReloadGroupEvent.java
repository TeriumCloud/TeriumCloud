package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupReload;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.Getter;

@Getter
public class ReloadGroupEvent extends Event {

    private final ICloudServiceGroup cloudServiceGroup;

    public ReloadGroupEvent(ICloudServiceGroup cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupReload(cloudServiceGroup));
    }
}