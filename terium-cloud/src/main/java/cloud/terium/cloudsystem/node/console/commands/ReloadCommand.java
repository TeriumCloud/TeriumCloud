package cloud.terium.cloudsystem.node.console.commands;

import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.config.ConfigManager;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.module.ILoadedModule;
import lombok.SneakyThrows;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reloading the cloud-system.", "rl");
    }

    @Override
    @SneakyThrows
    public void execute(String[] args) {
        Logger.log("Trying to reload terium-cloud.", LogType.INFO);
        Thread.sleep(500);
        Logger.log("Reloading all modules...", LogType.INFO);
        NodeStartup.getNode().getModuleProvider().getAllModules().stream().filter(ILoadedModule::isReloadable).forEach(module -> NodeStartup.getNode().getModuleProvider().reloadModule(module));

        Logger.log("Reloading config.json...", LogType.INFO);
        NodeStartup.getNode().setConfigManager(new ConfigManager());
        NodeStartup.getNode().setNodeConfig(NodeStartup.getNode().getConfigManager().toNodeConfig());

        Logger.log("Sending reloading packets...", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
        Thread.sleep(500);
        Logger.log("Successfully reloaded terium-cloud.", LogType.INFO);
    }
}
