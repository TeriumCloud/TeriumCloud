package cloud.terium.bridge.bukkit;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.listener.PlayerJoinListener;
import cloud.terium.bridge.bukkit.listener.PlayerQuitListener;
import cloud.terium.networking.json.DefaultJsonService;
import cloud.terium.networking.packets.PacketPlayOutSuccessfullServiceShutdown;
import cloud.terium.teriumapi.service.CloudServiceState;
import com.destroystokyo.paper.ParticleBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            TeriumBridge.getInstance().getCloudPlayerManager().getOnlinePlayers().forEach(iCloudPlayer -> Bukkit.broadcastMessage(iCloudPlayer.getUsername() + " / " + iCloudPlayer.getUniqueId() + " / " + iCloudPlayer.getConnectedCloudService().getServiceName()));
        }, 0, 20);

        new DefaultJsonService(teriumBridge.getThisName()).setServiceState(CloudServiceState.ONLINE);

        Runnable task = () -> {
            teriumBridge.getThisService().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
            teriumBridge.getThisService().setUsedMemory(teriumBridge.usedMemory());
            teriumBridge.getThisService().update();
        };

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        teriumBridge.getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullServiceShutdown(teriumBridge.getThisName()));
    }

    public static BridgeBukkitStartup getInstance() {
        return instance;
    }
}
