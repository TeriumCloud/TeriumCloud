package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.user.PermissionUser;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;

import java.util.HashMap;
import java.util.UUID;

public class PermissionPipeHandler implements Handler {

    public PermissionPipeHandler() {
        System.out.println("handler registered.");
    }

    @Override
    public void onReceive(Object object) {
        if (object instanceof PacketPlayOutSendHashMap packet) {
            HashMap<String, Object> hashMap = packet.hashMap();

            if (hashMap.containsKey("packet_key")) {
                switch ((String) hashMap.get("packet_key")) {
                    case "user_add" -> TeriumPermissionModule.getInstance().getPermissionUserManager().registerUser(new PermissionUser((String) hashMap.get("username"), (UUID) hashMap.get("uuid"), TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) hashMap.get("group")).orElseGet(null)));
                    case "user_update" -> TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId((UUID) hashMap.get("uuid")).orElseGet(null).setPermissionGroup(TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) hashMap.get("group_name")).orElseGet(null));
                }
            }
        }
    }
}
