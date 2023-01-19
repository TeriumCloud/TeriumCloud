package cloud.terium.plugin.impl.console;

import cloud.terium.networking.packet.console.PacketPlayOutRegisterCommand;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.console.command.ICommandFactory;

public class CommandFactory implements ICommandFactory {

    @Override
    public void registerCommand(Command command) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutRegisterCommand(command));
    }
}
