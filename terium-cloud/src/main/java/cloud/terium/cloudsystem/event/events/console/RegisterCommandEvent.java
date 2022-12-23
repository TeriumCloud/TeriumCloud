package cloud.terium.cloudsystem.event.events.console;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.console.PacketPlayOutRegisterCommand;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class RegisterCommandEvent extends Event {

    private final Command command;

    public RegisterCommandEvent(Command command) {
        this.command = command;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutRegisterCommand(command));
    }
}