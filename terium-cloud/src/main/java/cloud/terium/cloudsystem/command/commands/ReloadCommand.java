package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.manager.ConfigManager;
import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutReloadConfig;

public class ReloadCommand extends Command {

    public ReloadCommand(CommandManager commandManager) {
        super("reload");
        commandManager.register(this);
    }

    /*
     * TODO: Make that the service groups will reload but not overwirte
     */

    @Override
    public void execute(String[] args) {
        Logger.log("Trying to reload Terium...", LogType.INFO);
        Terium.getTerium().setConfigManager(new ConfigManager());
        Terium.getTerium().getServiceGroupManager().loadGroups(false);
        Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
        Logger.log("Successfully reloaded Terium.", LogType.INFO);
    }
}
