package cloud.terium.cloudsystem.common.event.events.group;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.group.PacketPlayOutGroupUpdate;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class GroupUpdateEvent extends Event {

    private final String cloudServiceGroup;

    public GroupUpdateEvent(String cloudServiceGroup) {
        this.cloudServiceGroup = cloudServiceGroup;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupUpdate(cloudServiceGroup));
    }
}