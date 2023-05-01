package cloud.terium.module.permission.cloud;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.user.UserFileManager;
import cloud.terium.module.permission.utils.ApplicationType;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;

import java.util.HashMap;
import java.util.UUID;

public class PermissionPipeHandler implements Handler {

    private final ApplicationType applicationType;

    public PermissionPipeHandler(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    @Override
    public void onReceive(Object object) {
        if (object instanceof PacketPlayOutSendHashMap packet) {
            HashMap<String, Object> hashMap = packet.hashMap();

            if (hashMap.containsKey("packet_key")) {
                switch ((String) hashMap.get("packet_key")) {
                    case "user_add" -> TeriumPermissionModule.getInstance().setUserFileManager(new UserFileManager(applicationType));
                    case "user_update" -> TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByUniquedId((UUID) hashMap.get("uuid")).orElseGet(null).setPermissionGroup(TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName((String) hashMap.get("group_name")).orElseGet(null));
                }
            }
        }
    }
}
