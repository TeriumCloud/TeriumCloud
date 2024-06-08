package cloud.terium.module.permission.permission.base;

import cloud.terium.module.permission.TeriumPermissionModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class TeriumPermissionBaseBungeeCord implements Listener {

    @EventHandler
    public void handle(PermissionCheckEvent event){
        if (event.getSender() instanceof ProxiedPlayer player) {
            List<String> permissions = TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(player.getUniqueId()).get().getPermissionGroup().permissions();

            event.setHasPermission((permissions.contains(event.getPermission()) || permissions.contains("*")));
        }
    }
}