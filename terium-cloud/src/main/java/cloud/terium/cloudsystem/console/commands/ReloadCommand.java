package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.config.ConfigManager;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.networking.packet.PacketPlayOutReloadConfig;
import cloud.terium.networking.packet.group.PacketPlayOutGroupsReload;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.module.ILoadedModule;
import lombok.SneakyThrows;

import java.io.File;

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
        TeriumCloud.getTerium().getModuleProvider().getAllModules().stream().filter(ILoadedModule::isReloadable).forEach(module -> TeriumCloud.getTerium().getModuleProvider().reloadModule(module));

        Logger.log("Reloading config.json...", LogType.INFO);
        TeriumCloud.getTerium().setConfigManager(new ConfigManager());
        TeriumCloud.getTerium().setCloudConfig(TeriumCloud.getTerium().getConfigManager().toCloudConfig());

        Logger.log("Sending reloading packets...", LogType.INFO);
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutReloadConfig());
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutGroupsReload());
        Thread.sleep(500);
        Logger.log("Successfully reloaded terium-cloud.", LogType.INFO);
    }
}
