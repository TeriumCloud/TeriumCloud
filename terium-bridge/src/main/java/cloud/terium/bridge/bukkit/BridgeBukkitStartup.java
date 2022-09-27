package cloud.terium.bridge.bukkit;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.commands.StopCommand;
import cloud.terium.bridge.bukkit.listener.PlayerJoinListener;
import cloud.terium.bridge.bukkit.listener.PlayerQuitListener;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.PacketPlayOutSuccessfullServiceShutdown;
import cloud.terium.teriumapi.service.CloudServiceState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BridgeBukkitStartup extends JavaPlugin {

    private static BridgeBukkitStartup instance;
    private static TeriumBridge teriumBridge;

    @Override
    public void onEnable() {
        instance = this;
        teriumBridge = new TeriumBridge();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        teriumBridge.initServices(this);
        teriumBridge.startSendingUsedMemory();

        getCommand("stop").setExecutor(new StopCommand());

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            TeriumBridge.getInstance().getCloudPlayerManager().getOnlinePlayers().forEach(iCloudPlayer -> Bukkit.broadcastMessage(iCloudPlayer.getUsername() + " / " + iCloudPlayer.getUniqueId() + " / " + iCloudPlayer.getConnectedCloudService().getServiceName()));
        }, 10, 0);

        new DefaultJsonService(teriumBridge.getThisName()).setServiceState(CloudServiceState.ONLINE);
    }

    @Override
    public void onDisable() {
        teriumBridge.getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullServiceShutdown(teriumBridge.getThisName()));
    }

    public static BridgeBukkitStartup getInstance() {
        return instance;
    }
}
