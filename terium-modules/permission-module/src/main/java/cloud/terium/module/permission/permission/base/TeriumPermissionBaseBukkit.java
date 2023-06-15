package cloud.terium.module.permission.permission.base;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.PermissionGroup;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TeriumPermissionBaseBukkit extends PermissibleBase {

    private final Player player;

    public TeriumPermissionBaseBukkit(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        PermissionGroup permissionGroup = TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(player.getUniqueId()).orElseGet(null).getPermissionGroup();
        if (Arrays.asList("bukkit.command.version", "bukkit.command.plugins").contains(inName)) return true;

        if (permissionGroup.permissions().contains("-" + inName)) return false;

        if (permissionGroup.permissions().contains("*") || player.isOp()) return true;

        return permissionGroup.permissions().contains(inName);
    }
}