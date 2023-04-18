package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.TeriumPermissionModule;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public class PermissionUserManager {

    private final HashMap<UUID, PermissionUser> loadedUsers;

    public PermissionUserManager() {
        this.loadedUsers = new LinkedHashMap<>();
    }

    public void registerUser(PermissionUser permissionUser) {
        loadedUsers.put(permissionUser.getUniquedId(), permissionUser);
    }

    public void registerNewUser(PermissionUser permissionUser) {
        loadedUsers.put(permissionUser.getUniquedId(), permissionUser);
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("username", permissionUser.getName());
        playerObject.addProperty("uuid", permissionUser.getUniquedId().toString());
        playerObject.addProperty("group", permissionUser.getPermissionGroup().name());
        TeriumPermissionModule.getInstance().getUserFileManager().getJson().add(permissionUser.getName(), playerObject);
        TeriumPermissionModule.getInstance().getUserFileManager().save();
    }

    public Optional<PermissionUser> getUserByName(String name) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.getName().equals(name)).findAny();
    }

    public Optional<PermissionUser> getUserByUniquedId(UUID uuid) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.getUniquedId().equals(uuid)).findAny();
    }
}