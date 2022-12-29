package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupUpdate;
import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import lombok.Getter;

@Getter
public class GroupUpdateEvent extends Event {

    private final ICloudServiceGroup cloudServiceGroup;

    public GroupUpdateEvent(ICloudServiceGroup cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupUpdate(cloudServiceGroup));
    }
}