package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reloading the cloud-system.", "rl");
    }

    @Override
    public void execute(String[] args) {
        Logger.log("Trying to reload terium-cloud.", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
        Logger.log("Successfully reloaded terium-cloud.", LogType.INFO);
    }
}
