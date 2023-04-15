package cloud.terium.module.permission.permission.group;

import cloud.terium.module.permission.utils.ApplicationType;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.*;

@Getter
public class PermissionGroupManager {

    private final HashMap<String, PermissionGroup> loadedGroups;

    public PermissionGroupManager() {
        this.loadedGroups = new LinkedHashMap<>();
    }

    public void registerGroup(PermissionGroup permissionGroup) {
        loadedGroups.put(permissionGroup.name(), permissionGroup);
    }

    public Optional<PermissionGroup> getGroupByName(String name) {
        return loadedGroups.values().stream().filter(permissionGroup -> permissionGroup.name().equals(name)).findAny();
    }

    public JsonObject getGroupJsonConfig(String group, ApplicationType applicationType) {
        return new GroupFileManager(group, applicationType).getJson();
    }
}