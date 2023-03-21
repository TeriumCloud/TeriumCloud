package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.group.PacketPlayOutGroupReload;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class ReloadGroupEvent extends Event {

    private final String cloudServiceGroup;

    public ReloadGroupEvent(String cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupReload(cloudServiceGroup));
    }
}