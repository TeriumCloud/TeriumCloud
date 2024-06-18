package cloud.terium.plugin.bukkit;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.plugin.bukkit.listener.PlayerCommandPreprocessListener;
import cloud.terium.plugin.bukkit.listener.PlayerJoinListener;
import cloud.terium.plugin.bukkit.listener.PlayerQuitListener;
import cloud.terium.teriumapi.service.ICloudService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public class TeriumBukkitStartup extends JavaPlugin {

    private static TeriumBukkitStartup instance;
    private TeriumExtension extension;

    @Override
    public void onLoad() {
        extension = new TeriumExtension() {
            @Override
            public void executeCommand(String command) {
                Bukkit.getScheduler().runTask(TeriumBukkitStartup.instance, () -> Bukkit.getCommandMap().dispatch(Bukkit.getConsoleSender(), command));
            }
        };
    }

    @Override
    public void onEnable() {
        extension.successfulStart();
        instance = this;
        Bukkit.getConsoleSender().sendMessage("§aStartup of bukkit terium-plugin...");

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerCommandPreprocessListener(), this);

        Bukkit.getConsoleSender().sendMessage("§aStartup of bukkit terium-plugin succeed...");
    }
}