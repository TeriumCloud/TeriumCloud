package cloud.terium.module.permission.pipe.packet;

import cloud.terium.module.permission.permission.user.PermissionUser;
import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutUserUpdate(PermissionUser permissionUser) implements Packet {
}