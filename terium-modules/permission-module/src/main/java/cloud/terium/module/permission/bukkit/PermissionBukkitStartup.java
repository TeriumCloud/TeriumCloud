package cloud.terium.module.permission.bukkit;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.base.TeriumPermissionBaseBukkit;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class PermissionBukkitStartup extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new TeriumPermissionModule().onEnable();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @SneakyThrows
    @EventHandler
    public void handlePlayerLogin(PlayerLoginEvent event) {
        try {
            Field field = reflectCraftClazz(".entity.CraftHumanEntity").getDeclaredField("perm");
            field.setAccessible(true);
            field.set(event.getPlayer(), new TeriumPermissionBaseBukkit(event.getPlayer()));
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.fillInStackTrace();
        }
    }

    private Class<?> reflectCraftClazz(String suffix) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Class.forName("org.bukkit.craftbukkit." + version + suffix);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                return Class.forName("org.bukkit.craftbukkit" + suffix);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}