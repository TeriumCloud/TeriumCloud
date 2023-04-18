package cloud.terium.module.permission.permission.group;

import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.TeriumAPI;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public record PermissionGroup(String name, String prefix, String suffix, String chatColor,
                              int property, boolean standard, List<String> permissions,
                              List<String> includedGroups) implements Serializable {


    public void addPermission(String permission) {
        this.permissions.add(permission);
        GroupFileManager groupFileManager = new GroupFileManager(name, TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN);
        groupFileManager.getJson().get("permissions").getAsJsonArray().add(permission);
        groupFileManager.save();
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
        GroupFileManager groupFileManager = new GroupFileManager(name, TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN);
        groupFileManager.getJson().get("permissions").getAsJsonArray().add(permission);
        groupFileManager.save();
    }
}