package cloud.terium.plugin.bukkit;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.bukkit.listener.PlayerCommandPreprocessListener;
import cloud.terium.plugin.bukkit.listener.PlayerJoinListener;
import cloud.terium.plugin.bukkit.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TeriumBukkitStartup extends JavaPlugin {

    private static TeriumBukkitStartup instance;

    public static TeriumBukkitStartup getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("§aStartup of bukkit terium-plugin...");
        try {
            new TeriumPlugin();
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new PlayerJoinListener(), this);
            pluginManager.registerEvents(new PlayerQuitListener(), this);
            pluginManager.registerEvents(new PlayerCommandPreprocessListener(), this);

            Bukkit.getConsoleSender().sendMessage("§aStartup of bukkit terium-plugin successed...");
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("§cStartup of bukkit terium-plugin failed...");
            Bukkit.getConsoleSender().sendMessage("§7Exception message§f: §c" + exception.getMessage());
        }
    }
}