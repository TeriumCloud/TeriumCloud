package cloud.terium.module.permission.pipe.packet;

import cloud.terium.module.permission.permission.group.PermissionGroup;
import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutGroupUpdate(PermissionGroup permissionGroup) implements Packet {
}