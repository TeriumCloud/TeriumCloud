package cloud.terium.module.permission.velocity.command;

import cloud.terium.module.permission.TeriumPermissionModule;
import cloud.terium.module.permission.permission.user.PermissionUser;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.packets.PacketPlayOutSendHashMap;
import cloud.terium.teriumapi.template.ITemplate;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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
                .build();

        return new BrigadierCommand(commandNode);
    }

    private int help(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("All <#06bdf8>/cperms <white>commands:"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>user <user> setgroup <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" <gray>● <#06bdf8>/cperms <white>group list"));
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

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packet_key", "user_update");
            hashMap.put("uuid", permissionUser.getUniquedId());
            hashMap.put("group_name", permissionGroup.name());
            TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendHashMap(hashMap));
            TeriumPermissionModule.getInstance().getUserFileManager().getJson().get(permissionUser.getUniquedId().toString()).getAsJsonObject().addProperty("group", permissionGroup.name());
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully set group of <#06bdf8>" + permissionUser.getName() + " <white>to <#06bdf8>" + permissionGroup.name() + "<gray>."));
        }, () -> context.getSource().sendMessage(Component.text("§cThis group isn't registered."))), () -> context.getSource().sendMessage(Component.text("§cThis user isn't registered.")));

        return 1;
    }
}
