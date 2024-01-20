package cloud.terium.module.permission.permission.group;

import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.TeriumAPI;
import com.google.gson.JsonArray;

import java.io.Serializable;
import java.util.List;

public record PermissionGroup(String name, String prefix, String suffix, String chatColor,
                              int property, boolean standard, List<String> permissions,
                              List<String> includedGroups) implements Serializable {


    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            this.permissions.add(permission);

            if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null && !permissions.contains(permission)) {
                GroupFileManager groupFileManager = new GroupFileManager(name, TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN);
                groupFileManager.loadFile();
                groupFileManager.getJson().get("permissions").getAsJsonArray().add(permission);
                groupFileManager.save();
            }
        }
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);

        if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
            GroupFileManager groupFileManager = new GroupFileManager(name, TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN);
            groupFileManager.loadFile();
            groupFileManager.getJson().get("permissions").getAsJsonArray().forEach(jsonElement -> {
                if (jsonElement.getAsString().equals(permission)) {
                    JsonArray jsonArray = groupFileManager.getJson().get("permissions").getAsJsonArray().deepCopy();
                    jsonArray.remove(jsonElement);
                    groupFileManager.getJson().add("permissions", jsonArray);
                }
            });
            groupFileManager.save();
        }
    }
}