package cloud.terium.cloudsystem.event.events.group;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.group.PacketPlayOutGroupUpdate;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class GroupUpdateEvent extends Event {

    private final String servicegroup;
    private final boolean maintenance;
    private final boolean isStatic;
    private final int maxPlayers;
    private final int memory;
    private final int minServices;
    private final int maxServices;

    public GroupUpdateEvent(String servicegroup, boolean maintenance, boolean isStatic, int maxPlayers, int memory, int minServices, int maxServices) {
        this.servicegroup = servicegroup;
        this.maintenance = maintenance;
        this.isStatic = isStatic;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.minServices = minServices;
        this.maxServices = maxServices;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutGroupUpdate(servicegroup, maintenance, isStatic, maxPlayers, memory, minServices, maxServices));
    }
}