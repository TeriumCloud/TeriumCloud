package cloud.terium.module.permission.bungeecord.command;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.base.TeriumPermissionBaseBungeeCord;
import cloud.terium.module.permission.permission.group.PermissionGroup;
import cloud.terium.module.permission.bungeecord.PermissionBungeeCordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendString;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CloudPermissionsCommand extends Command {

    public CloudPermissionsCommand() {
        super("cperms", "terium.command.permissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "user":
                handleUserCommand(sender, args);
                break;
            case "group":
                handleGroupCommand(sender, args);
                break;
            case "groups":
                groupList(sender);
                break;
            case "reload":
                reload(sender);
                break;
            default:
                help(sender);
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage(new TextComponent("All §b/cperms §fcommands:"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms user <user>"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms user <user> setgroup <group>"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms groups"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms group <group>"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms group <group> create"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms group <group> add permission <permission>"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms group <group> remove permission <permission>"));
        sender.sendMessage(new TextComponent(" §7● §f /cperms reload"));
    }

    private void handleUserCommand(CommandSender sender, String[] args) {
        if (args.length == 2) {
            userInfo(sender, args[1]);
        } else if (args.length == 4 && args[2].equalsIgnoreCase("setgroup")) {
            userSetGroup(sender, args[1], args[3]);
        } else {
            help(sender);
        }
    }

    private void handleGroupCommand(CommandSender sender, String[] args) {
        if (args.length == 2) {
            groupInfo(sender, args[1]);
        } else if (args.length == 3 && args[2].equalsIgnoreCase("create")) {
            createGroup(sender, args[1]);
        } else if (args.length == 5) {
            if (args[2].equalsIgnoreCase("add") && args[3].equalsIgnoreCase("permission")) {
                addPermission(sender, args[1], args[4]);
            } else if (args[2].equalsIgnoreCase("remove") && args[3].equalsIgnoreCase("permission")) {
                removePermission(sender, args[1], args[4]);
            } else {
                help(sender);
            }
        } else {
            help(sender);
        }
    }

    private void userInfo(CommandSender sender, String username) {
        TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByName(username).ifPresentOrElse(permissionUser -> {
            sender.sendMessage(new TextComponent("Information about " + permissionUser.getName() + ":"));
            sender.sendMessage(new TextComponent(" §7● §f UUID: " + permissionUser.getUniquedId()));

            TextComponent component = new TextComponent(" §7● §f Group: " + permissionUser.getPermissionGroup().name());
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cperms group " + permissionUser.getPermissionGroup().name()));
            sender.sendMessage(component);
        }, () -> sender.sendMessage(new TextComponent("This user isn't registered.")));
    }

    private void userSetGroup(CommandSender sender, String username, String groupName) {
        TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByName(username).ifPresentOrElse(permissionUser -> {
            TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(groupName).ifPresentOrElse(permissionGroup -> {
                permissionUser.setPermissionGroup(permissionGroup);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("packet_type", "update_user");
                hashMap.put("uuid", permissionUser.getUniquedId().toString());
                hashMap.put("group_name", permissionGroup.name());

                sender.sendMessage(new TextComponent("Successfully set group of " + permissionUser.getName() + " to " + permissionGroup.name() + "."));
                if(PermissionBungeeCordStartup.getInstance().getProxy().getPlayer(username) != null) {
                    ProxiedPlayer player = PermissionBungeeCordStartup.getInstance().getProxy().getPlayer(username);
                    BungeeAudiences.create(PermissionBungeeCordStartup.getInstance()).player(player).sendMessage(MiniMessage.miniMessage().deserialize(TeriumPermissionModule.getInstance().getConfigManager().getJson().get("new_rank.message").getAsString().replace("%rank%", permissionGroup.name())));
                }
                PermissionBungeeCordStartup.getInstance().getProxy().getScheduler().schedule(PermissionBungeeCordStartup.getInstance(), () -> TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap)), 1, TimeUnit.SECONDS);
            }, () -> sender.sendMessage(new TextComponent("This group isn't registered.")));
        }, () -> sender.sendMessage(new TextComponent("This user isn't registered.")));
    }

    private void groupList(CommandSender sender) {
        sender.sendMessage(new TextComponent("All loaded permission groups:"));
        List<PermissionGroup> toSort = new ArrayList<>();
        for (PermissionGroup permissionGroup : TeriumPermissionModule.getInstance().getPermissionGroupManager().getLoadedGroups().values()) {
            toSort.add(permissionGroup);
        }
        toSort.sort(Comparator.comparing(PermissionGroup::name));
        for (PermissionGroup permissionGroup : toSort) {
            TextComponent component = new TextComponent(" §7● §f " + permissionGroup.name());
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cperms group " + permissionGroup.name()));
            sender.sendMessage(component);
        }
    }

    private void createGroup(CommandSender sender, String groupName) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(groupName).ifPresentOrElse(permissionGroup -> {
            sender.sendMessage(new TextComponent("A group with that name is already registered."));
        }, () -> {
            TeriumPermissionModule.getInstance().getPermissionGroupManager().createPermissionGroup(groupName);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_type", "create_group");
            hashMap.put("group_name", groupName);
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));

            sender.sendMessage(new TextComponent("Successfully created group " + groupName + "."));
        });
    }

    private void groupInfo(CommandSender sender, String groupName) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(groupName).ifPresentOrElse(permissionGroup -> {
            sender.sendMessage(new TextComponent("Information about " + permissionGroup.name() + ":"));
            sender.sendMessage(new TextComponent(" §7● §f Prefix: " + permissionGroup.prefix()));
            sender.sendMessage(new TextComponent(" §7● §f Suffix: " + permissionGroup.suffix()));
            sender.sendMessage(new TextComponent(" §7● §f Chat color: " + permissionGroup.chatColor()));
            sender.sendMessage(new TextComponent(" §7● §f Standard: " + (permissionGroup.standard() ? "Yes" : "No")));
            if (permissionGroup.permissions().isEmpty()) {
                sender.sendMessage(new TextComponent(" §7● §f Permissions: None"));
            } else {
                sender.sendMessage(new TextComponent(" §7● §f Permissions: " + permissionGroup.permissions().size()));
                permissionGroup.permissions().forEach(permission -> sender.sendMessage(new TextComponent("   §7● §f " + permission)));
            }
            sender.sendMessage(new TextComponent(" §7● §f Included groups: " + permissionGroup.includedGroups()));
        }, () -> sender.sendMessage(new TextComponent("This group isn't registered.")));
    }

    private void addPermission(CommandSender sender, String groupName, String permission) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(groupName).ifPresentOrElse(permissionGroup -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_type", "add_permission");
            hashMap.put("group_name", groupName);
            hashMap.put("permission", permission);
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
            sender.sendMessage(new TextComponent("Successfully added permission '" + permission + "' to group '" + groupName + "'."));
        }, () -> sender.sendMessage(new TextComponent("This group isn't registered.")));
    }

    private void removePermission(CommandSender sender, String groupName, String permission) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(groupName).ifPresentOrElse(permissionGroup -> {
            if (permissionGroup.permissions().contains(permission)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("packet_type", "remove_permission");
                hashMap.put("group_name", groupName);
                hashMap.put("permission", permission);
                TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
                sender.sendMessage(new TextComponent("Successfully removed permission '" + permission + "' from group '" + groupName + "'."));
            } else {
                sender.sendMessage(new TextComponent("This permission isn't registered on the group."));
            }
        }, () -> sender.sendMessage(new TextComponent("This group isn't registered.")));
    }

    private void reload(CommandSender sender) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendString("reload_system"));
        sender.sendMessage(new TextComponent("Successfully reloaded permission-module."));
    }
}