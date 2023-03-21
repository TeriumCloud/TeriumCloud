package cloud.terium.cloudsystem.common.event.events.console;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.console.PacketPlayOutRegisterCommand;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class RegisterCommandEvent extends Event {

    private final Command command;

    public RegisterCommandEvent(Command command) {
        this.command = command;
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutRegisterCommand(command));
    }
}