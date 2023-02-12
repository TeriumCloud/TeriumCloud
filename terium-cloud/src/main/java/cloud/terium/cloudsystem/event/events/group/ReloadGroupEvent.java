package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupReload;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ReloadGroupEvent extends Event {

    private final String cloudServiceGroup;

    public ReloadGroupEvent(String cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupReload(cloudServiceGroup));
    }
}