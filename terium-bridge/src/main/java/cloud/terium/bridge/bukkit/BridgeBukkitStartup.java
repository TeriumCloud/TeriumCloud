package cloud.terium.bridge.bukkit;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.bukkit.commands.StopCommand;
import cloud.terium.bridge.bukkit.listener.PlayerJoinListener;
import cloud.terium.bridge.bukkit.listener.PlayerQuitListener;
import cloud.terium.networking.packets.PacketPlayOutSuccessfullServiceShutdown;
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

        getCommand("stop").setExecutor(new StopCommand());
    }

    @Override
    public void onDisable() {
        teriumBridge.getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutSuccessfullServiceShutdown(teriumBridge.getThisName()));
    }

    public static BridgeBukkitStartup getInstance() {
        return instance;
    }
}
