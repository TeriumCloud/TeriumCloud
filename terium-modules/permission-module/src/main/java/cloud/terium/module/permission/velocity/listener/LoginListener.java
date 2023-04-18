package cloud.terium.module.permission.velocity.listener;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.user.PermissionUser;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(event.getPlayer().getUniqueId()).ifPresentOrElse(player -> {}, () -> TeriumPermissionModule.getInstance().getPermissionUserManager().registerNewUser(new PermissionUser(event.getPlayer().getUsername(), event.getPlayer().getUniqueId(), TeriumPermissionModule.getInstance().getPermissionGroupManager().getDefaultGroup().orElseGet(null))));
    }
}
