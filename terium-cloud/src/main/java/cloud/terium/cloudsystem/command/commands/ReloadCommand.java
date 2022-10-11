package cloud.terium.cloudsystem.command.commands;

import cloud.terium.cloudsystem.Terium;
import cloud.terium.cloudsystem.command.Command;
import cloud.terium.cloudsystem.manager.CommandManager;
import cloud.terium.cloudsystem.manager.ConfigManager;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packets.PacketPlayOutReloadConfig;

public class ReloadCommand extends Command {

    public ReloadCommand(CommandManager commandManager) {
        super("reload");
        commandManager.register(this);
    }

    /*
     * TODO: Fix #reloadGroups() in ServiceGroupManager.
     */
    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("groups")) {
                Logger.log("Terium is trying to reload all service groups...", LogType.INFO);
            } else if (args[0].equalsIgnoreCase("config")) {
                Logger.log("Terium is trying to reload the config.json...", LogType.INFO);
                Terium.getTerium().setConfigManager(new ConfigManager());
                Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
                Logger.log("Successfully reloaded config.json.", LogType.INFO);
            } else if (args[0].equalsIgnoreCase("all")) {
                Logger.log("Trying to reload Terium...", LogType.INFO);
                Terium.getTerium().setConfigManager(new ConfigManager());
                Terium.getTerium().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
                Logger.log("Successfully reloaded config.json.", LogType.INFO);
                Logger.log("Successfully reloaded Terium.", LogType.INFO);
            }
        } else {
            Logger.log("Syntax: reload <groups/config/all>", LogType.INFO);
        }
    }
}
