package cloud.terium.module.permission.permission.group;

import java.io.Serializable;
import java.util.List;

public record PermissionGroup(String name, String prefix, String suffix, String tablist, String chatColor,
                              int property, List<String> permissions,
                              List<String> includedGroups) implements Serializable {
}
