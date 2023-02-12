package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupDelete;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class DeleteGroupEvent extends Event {

    private final String cloudServiceGroup;

    public DeleteGroupEvent(String cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupDelete(cloudServiceGroup));
    }
}