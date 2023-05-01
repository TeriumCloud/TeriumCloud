package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.PermissionGroup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
@Setter
@AllArgsConstructor
public class PermissionUser implements Serializable, Supplier<PermissionUser> {

    private String name;
    private final UUID uniquedId;
    private PermissionGroup permissionGroup;

    @Override
    public PermissionUser get() {
        return this;
    }
}