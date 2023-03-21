package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.config.ConfigManager;
import cloud.terium.cloudsystem.cluster.utils.Logger;
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
        ClusterStartup.getCluster().getModuleProvider().getAllModules().stream().filter(ILoadedModule::isReloadable).forEach(module -> ClusterStartup.getCluster().getModuleProvider().reloadModule(module));

        Logger.log("Reloading config.json...", LogType.INFO);
        ClusterStartup.getCluster().setConfigManager(new ConfigManager());
        ClusterStartup.getCluster().setCloudConfig(ClusterStartup.getCluster().getConfigManager().toCloudConfig());

        Logger.log("Sending reloading packets...", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
        Thread.sleep(500);
        Logger.log("Successfully reloaded terium-cloud.", LogType.INFO);
    }
}
