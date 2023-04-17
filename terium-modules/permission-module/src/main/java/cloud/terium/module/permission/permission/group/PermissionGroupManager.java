package cloud.terium.module.permission.permission.group;

import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.TeriumAPI;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.File;
import java.util.*;

@Getter
public class PermissionGroupManager {

    private final HashMap<String, PermissionGroup> loadedGroups;

    public PermissionGroupManager() {
        this.loadedGroups = new LinkedHashMap<>();
        if (!new File("modules/permission/groups").exists()) {
            new File("modules/permission/groups").mkdirs();
            createPermissionGroup("default", "§7§lDEFAULT ", "", "WHITE", 99, true, new LinkedList<>(), new LinkedList<>());
            createPermissionGroup("Admin", "§4§lADMIN ", "", "WHITE", 10, false, Arrays.asList("*", "terium.command.*"), new LinkedList<>());
        }
    }

    public void registerGroup(PermissionGroup permissionGroup) {
        loadedGroups.put(permissionGroup.name(), permissionGroup);
    }

    public PermissionGroup createPermissionGroup(String name, String prefix, String suffix, String chatColor,
                                                 int property, boolean standard, List<String> permissions,
                                                 List<String> includedGroups) {
        PermissionGroup permissionGroup = new PermissionGroup(name, prefix, suffix, chatColor, property, standard, permissions, includedGroups);
        registerGroup(permissionGroup);
        new GroupFileManager(name, TeriumAPI.getTeriumAPI().getProvider().getThisService() == null ? ApplicationType.MODULE : ApplicationType.PLUGIN).createFile(permissionGroup);
        return permissionGroup;
    }

    public Optional<PermissionGroup> getGroupByName(String name) {
        return loadedGroups.values().stream().filter(permissionGroup -> permissionGroup.name().equals(name)).findAny();
    }

    public Optional<PermissionGroup> getDefaultGroup() {
        return loadedGroups.values().stream().filter(PermissionGroup::standard).findAny();
    }

    public JsonObject getGroupJsonConfig(String group, ApplicationType applicationType) {
        return new GroupFileManager(group, applicationType).getJson();
    }
}