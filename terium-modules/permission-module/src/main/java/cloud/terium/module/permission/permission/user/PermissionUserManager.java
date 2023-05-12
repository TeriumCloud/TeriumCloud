package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
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
        if (loadedUsers.containsKey(permissionUser.getUniquedId()))
            loadedUsers.remove(permissionUser.getUniquedId());

        loadedUsers.put(permissionUser.getUniquedId(), permissionUser);
    }

    public void registerNewUser(PermissionUser permissionUser) {
        loadedUsers.put(permissionUser.getUniquedId(), permissionUser);
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("username", permissionUser.getName());
        playerObject.addProperty("uuid", permissionUser.getUniquedId().toString());
        playerObject.addProperty("group", permissionUser.getPermissionGroup().name());
        TeriumPermissionModule.getInstance().getUserFileManager().getJson().add(permissionUser.getUniquedId().toString(), playerObject);
        TeriumPermissionModule.getInstance().getUserFileManager().save();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("packet_key", "user_add");
        hashMap.put("username", permissionUser.getName());
        hashMap.put("uuid", permissionUser.getUniquedId());
        hashMap.put("group", permissionUser.getPermissionGroup().name());
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
    }

    public Optional<PermissionUser> getUserByName(String name) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.getName().equals(name)).findAny();
    }

    public Optional<PermissionUser> getUserByUniquedId(UUID uuid) {
        return loadedUsers.values().stream().filter(permissionUser -> permissionUser.getUniquedId().equals(uuid)).findAny();
    }
}