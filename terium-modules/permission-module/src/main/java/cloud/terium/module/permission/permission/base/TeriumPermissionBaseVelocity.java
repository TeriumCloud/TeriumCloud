package cloud.terium.module.permission.permission.base;

import cloud.terium.module.permission.TeriumPermissionModule;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;

public class TeriumPermissionBaseVelocity implements PermissionProvider {

    @Override
    public PermissionFunction createFunction(PermissionSubject subject) {
        if (!(subject instanceof Player)) return permission -> null;
        return new VelocityPermissionFunction((Player) subject);
    }

    private record VelocityPermissionFunction(Player player) implements PermissionFunction {
        @Override
        public Tristate getPermissionValue(String permission) {
            if (TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(player.getUniqueId()).isEmpty())
                return Tristate.fromBoolean(false);

            return Tristate.fromBoolean(TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(player.getUniqueId()).orElseGet(null).getPermissionGroup().permissions().contains(permission) || TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(player.getUniqueId()).orElseGet(null).getPermissionGroup().permissions().contains("*"));
        }
    }
}
