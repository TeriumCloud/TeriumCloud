package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.permission.group.PermissionGroup;

import java.util.UUID;

public record PermissionUser(String name, UUID uniquedId, PermissionGroup permissionGroup) {
}