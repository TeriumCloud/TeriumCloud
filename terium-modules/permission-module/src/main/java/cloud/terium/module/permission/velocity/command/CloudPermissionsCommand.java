package cloud.terium.module.permission.velocity.command;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.group.PermissionGroup;
import cloud.terium.module.permission.velocity.PermissionVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendString;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.print.DocFlavor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CloudPermissionsCommand {

    public BrigadierCommand build(String name) {
        LiteralCommandNode<CommandSource> commandNode = LiteralArgumentBuilder.<CommandSource>literal(name)
                .requires(commandSource -> commandSource.hasPermission("terium.command.permissions"))
                .executes(this::help)
                .then(LiteralArgumentBuilder.<CommandSource>literal("user")
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("user", StringArgumentType.string())
                                .suggests(this::playerSuggestion)
                                .executes(this::userInfo)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("setgroup")
                                        .executes(this::help)
                                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("group", StringArgumentType.string())
                                                .executes(this::help)
                                                .suggests(this::groupSuggestion)
                                                .executes(this::userSetGroup)))))
                .then(LiteralArgumentBuilder.<CommandSource>literal("group")
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("group", StringArgumentType.string())
                                .executes(this::groupInfo)
                                .suggests(this::groupSuggestion)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("create")
                                        .executes(this::createGroup))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("add")
                                        .executes(this::help)
                                        .then(LiteralArgumentBuilder.<CommandSource>literal("permission")
                                                .executes(this::help)
                                                .then(RequiredArgumentBuilder.<CommandSource, String>argument("permission", StringArgumentType.string())
                                                        .executes(this::addPermission))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("remove")
                                        .executes(this::help)
                                        .then(LiteralArgumentBuilder.<CommandSource>literal("permission")
                                                .executes(this::help)
                                                .then(RequiredArgumentBuilder.<CommandSource, String>argument("permission", StringArgumentType.string())
                                                        .suggests(this::permissionSuggest)
                                                        .executes(this::removePermission))))))
                .then(LiteralArgumentBuilder.<CommandSource>literal("groups")
                        .executes(this::groupList))
                .build();

        return new BrigadierCommand(commandNode);
    }

    private int help(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("All <#06bdf8>/cperms <white>commands:"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user> setgroup <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>groups"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> create"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> add permission <permission>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group <group> remove permission <permission>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>reload"));

        return 1;
    }

    private CompletableFuture<Suggestions> playerSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().forEach(player -> suggestionsBuilder.suggest(player.getUsername()));
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> groupSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getLoadedGroups().values().forEach(permissionGroup -> suggestionsBuilder.suggest(permissionGroup.name()));
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> permissionSuggest(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).get().permissions().forEach(suggestionsBuilder::suggest);
        return suggestionsBuilder.buildFuture();
    }

    private int userInfo(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByName(context.getArgument("user", String.class)).ifPresentOrElse(permissionUser -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Information about <#06bdf8>" + permissionUser.getName() + "<white>:"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>UUID: " + permissionUser.getUniquedId()));

            Component component = MiniMessage.miniMessage().deserialize("  <gray>● <white>Group: " + permissionUser.getPermissionGroup().name());
            component.clickEvent(ClickEvent.runCommand("/cperms group " + permissionUser.getPermissionGroup().name()));
            component.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§fClick to get more information.")));

            context.getSource().sendMessage(component);
        }, () -> context.getSource().sendMessage(Component.text("§cThis user isn't registered.")));

        return 1;
    }

    private int userSetGroup(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionUserManager().getUserByName(context.getArgument("user", String.class)).ifPresentOrElse(permissionUser -> TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(permissionGroup -> {
            permissionUser.setPermissionGroup(permissionGroup);

            permissionUser.setPermissionGroup(permissionGroup);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_type", "update_user");
            hashMap.put("uuid", permissionUser.getUniquedId().toString());
            hashMap.put("group_name", permissionGroup.name());

            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully set group of <#06bdf8>" + permissionUser.getName() + " <green>to <#06bdf8>" + permissionGroup.name() + "<gray>."));
            PermissionVelocityStartup.getInstance().getProxyServer().getPlayer(context.getArgument("user", String.class)).ifPresent(player -> player.disconnect(Component.text("New Rank: " + permissionGroup.name())));
            PermissionVelocityStartup.getInstance().getProxyServer().getScheduler().buildTask(PermissionVelocityStartup.getInstance(), () -> TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap))).delay(1, TimeUnit.SECONDS).schedule();
        }, () -> context.getSource().sendMessage(Component.text("§cThis group isn't registered."))), () -> context.getSource().sendMessage(Component.text("§cThis user isn't registered.")));

        return 1;
    }

    private int groupList(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("All <#06bdf8>loaded <white>permission groups:"));
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getLoadedGroups().values().stream().sorted(Comparator.comparing(PermissionGroup::name)).forEach(permissionGroup -> {
            Component component = MiniMessage.miniMessage().deserialize("  <gray>● <#06bdf8>" + permissionGroup.name());
            component.clickEvent(ClickEvent.runCommand("/cperms group " + permissionGroup.name()));
            component.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§fClick to get more information.")));

            context.getSource().sendMessage(component);
        });

        return 1;
    }

    private int createGroup(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(permissionGroup -> context.getSource().sendMessage(Component.text("§cA group with that name is already registered.")), () -> {
            TeriumPermissionModule.getInstance().getPermissionGroupManager().createPermissionGroup(context.getArgument("group", String.class));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_type", "create_group");
            hashMap.put("group_name", context.getArgument("group", String.class));
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));

            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully create group <#06bdf8>" + context.getArgument("group", String.class) + "<gray>."));
        });
        return 1;
    }

    private int groupInfo(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(permissionGroup -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Information about <#06bdf8>" + permissionGroup.name() + "<white>:"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Prefix: " + permissionGroup.prefix()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Suffix: " + permissionGroup.suffix()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Chat color: " + permissionGroup.chatColor()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Standard: " + (permissionGroup.standard() ? "<green>Yes" : "<red>No")));
            if (permissionGroup.permissions().isEmpty()) {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Permissions: <red>None"));
            } else {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Permissions: <green>" + permissionGroup.permissions().size()));
                permissionGroup.permissions().forEach(s -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("     <gray>● <white>" + s)));
            }
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Included groups: " + permissionGroup.includedGroups()));

        }, () -> context.getSource().sendMessage(Component.text("§cThis user isn't registered.")));

        return 1;
    }

    private int addPermission(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(permissionGroup -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_type", "add_permission");
            hashMap.put("group_name", context.getArgument("group", String.class));
            hashMap.put("permission", context.getArgument("permission", String.class));
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully added permission <gray>'<#06bdf8>" + context.getArgument("permission", String.class) + "<gray>' <green>to group <gray>'<#06bdf8>" + context.getArgument("group", String.class) + "<gray>'."));
        }, () -> context.getSource().sendMessage(Component.text("§cThis group isn't registered.")));

        return 1;
    }

    private int removePermission(CommandContext<CommandSource> context) {
        TeriumPermissionModule.getInstance().getPermissionGroupManager().getGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(permissionGroup -> {
            if(permissionGroup.permissions().contains(context.getArgument("permission", String.class))) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("packet_type", "remove_permission");
                hashMap.put("group_name", context.getArgument("group", String.class));
                hashMap.put("permission", context.getArgument("permission", String.class));
                TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully removed permission <gray>'<#06bdf8>" + context.getArgument("permission", String.class) + "<gray>' <green>to group <gray>'<#06bdf8>" + context.getArgument("group", String.class) + "<gray>'."));
            } else {
                context.getSource().sendMessage(Component.text("§4This permission isn't registered on the group."));
            }
        }, () -> context.getSource().sendMessage(Component.text("§cThis group isn't registered.")));

        return 1;
    }
}
