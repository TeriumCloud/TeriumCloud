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

@Getter
public class BridgeBukkitStartup extends JavaPlugin {

    private static BridgeBukkitStartup instance;
    private static TeriumBridge teriumBridge;
    private double var = 0;
    private ParticleBuilder particleBuilder;
    private ParticleBuilder particleBuilder1;
    private Location location;
    private Location first;
    private Location secound;

    @Override
    public void onEnable() {
        instance = this;
        teriumBridge = new TeriumBridge();
        particleBuilder = new ParticleBuilder(Particle.SOUL_FIRE_FLAME);
        particleBuilder1 = new ParticleBuilder(Particle.SOUL_FIRE_FLAME);
        particleBuilder.extra(0);
        particleBuilder1.extra(0);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        teriumBridge.initServices(this);
        teriumBridge.startSendingUsedMemory();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            TeriumBridge.getInstance().getCloudPlayerManager().getOnlinePlayers().forEach(iCloudPlayer -> Bukkit.broadcastMessage(iCloudPlayer.getUsername() + " / " + iCloudPlayer.getUniqueId() + " / " + iCloudPlayer.getConnectedCloudService().getServiceName()));
        }, 0, 20);

        new DefaultJsonService(teriumBridge.getThisName()).setServiceState(CloudServiceState.ONLINE);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            var += Math.PI / 16;
            Bukkit.getOnlinePlayers().forEach(current -> {
                if(current.getName().equals("ByRaudy")) {
                    location = current.getLocation();
                    first = location.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                    secound = location.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                    particleBuilder.location(first.setDirection(first.getDirection()));
                    particleBuilder1.location(secound.setDirection(secound.getDirection()));
                    particleBuilder.spawn();
                    particleBuilder1.spawn();
                    //ParticleEffect.SPELL_INSTANT.display(first, 0, 0, 0, 0, 10, null, current);
                    //ParticleEffect.SPELL_INSTANT.display(secound, 0, 0, 0, 0, 10, null, current);
                    //ParticleEffect.SPELL_WITCH.display(first, 0, 0, 0, 0, 10, null, current);
                    //ParticleEffect.SPELL_WITCH.display(secound, 0, 0, 0, 0, 10, null, current);
                }
            });
        }, 0, 1);
    }

    @Override
    public void onDisable() {
        teriumBridge.getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullServiceShutdown(teriumBridge.getThisName()));
    }

    public static BridgeBukkitStartup getInstance() {
        return instance;
    }
}
