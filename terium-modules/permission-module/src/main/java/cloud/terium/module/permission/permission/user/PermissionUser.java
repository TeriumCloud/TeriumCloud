package cloud.terium.module.permission.permission.user;

import cloud.terium.module.permission.permission.group.PermissionGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PermissionUser implements Serializable {

    private String name;
    private final UUID uniquedId;
    private PermissionGroup permissionGroup;
}