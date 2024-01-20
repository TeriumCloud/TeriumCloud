package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.PermissionGroup;
import cloud.terium.module.permission.permission.user.PermissionUser;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendString;

import java.util.LinkedList;
import java.util.UUID;

public class PermissionPipeHandler implements Handler {

    @Override
    public void onReceive(Object object) {
        if (object instanceof PacketPlayOutSendString packet) {
            if (packet.string().equals("reload_system"))
                TeriumPermissionModule.getInstance().reload();
        }

        if (object instanceof PacketPlayOutSendHashMap packet) {
            if (packet.hashMap().containsValue("update_user")) {
                TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId(UUID.fromString((String) packet.hashMap().get("uuid"))).ifPresent(permissionUser ->
                        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) packet.hashMap().get("group_name")).ifPresent(permissionGroup -> {
                            permissionUser.setPermissionGroup(permissionGroup);

                            if (TeriumAPI.getTeriumAPI().getProvider().getThisService() == null) {
                                TeriumPermissionModule.getInstance().getUserFileManager().getJson().get(permissionUser.getUniquedId().toString()).getAsJsonObject().addProperty("group", permissionGroup.name());
                                TeriumPermissionModule.getInstance().getUserFileManager().save();
                            }
                        }));

            }

            if (packet.hashMap().containsValue("user_add")) {
                PermissionUser permissionUser = new PermissionUser((UUID) packet.hashMap().get("uuid"), (String) packet.hashMap().get("username"), TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) packet.hashMap().get("group")).orElseGet(null));
                TeriumPermissionModule.getInstance().getPermissionUserManager().registerUser(permissionUser);
            }

            if (packet.hashMap().containsValue("create_group")) {
                TeriumPermissionModule.getInstance().getPermissionGroupManager().registerGroup(new PermissionGroup((String) packet.hashMap().get("group_name"), "<white><bold>" + packet.hashMap().get("group_name") + "</bold>", "", "WHITE", 99, false, new LinkedList<>(), new LinkedList<>()));
            }

            if (packet.hashMap().containsValue("add_permission")) {
                TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) packet.hashMap().get("group_name")).ifPresent(permissionGroup -> {
                    permissionGroup.addPermission((String) packet.hashMap().get("permission"));
                });
            }

            if (packet.hashMap().containsValue("remove_permission")) {
                TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) packet.hashMap().get("group_name")).ifPresent(permissionGroup -> {
                    permissionGroup.removePermission((String) packet.hashMap().get("permission"));
                });
            }
        }
    }
}