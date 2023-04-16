package cloud.terium.module.permission.permission.user;

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
        loadedUsers.put(permissionUser.uniquedId(), permissionUser);
    }

    public Optional<PermissionUser> getUserByName(String name) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.name().equals(name)).findAny();
    }

    public Optional<PermissionUser> getUserByUniquedId(UUID uuid) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.uniquedId().equals(uuid)).findAny();
    }
}