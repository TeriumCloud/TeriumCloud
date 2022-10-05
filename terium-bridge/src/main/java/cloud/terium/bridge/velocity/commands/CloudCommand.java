package cloud.terium.bridge.velocity.commands;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutServiceLock;
import cloud.terium.networking.packets.PacketPlayOutServiceShutdown;
import cloud.terium.networking.packets.PacketPlayOutServiceStart;
import cloud.terium.networking.packets.PacketPlayOutServiceUnlock;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.group.ICloudServiceGroup;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.print.DocFlavor;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class CloudCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("cloud")
                //.requires(commandSource -> commandSource.hasPermission("terium.cloudcommand"))
                .executes(this::sendHelp)
                // Service
                .then(LiteralArgumentBuilder.<CommandSource>literal("service")
                        .executes(this::sendServiceHelp)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                .suggests(this::suggestServices)
                                .executes(this::sendServiceHelp)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("info")
                                        .executes(context -> serviceInfo(context, TeriumAPI.getTeriumAPI().getServiceManager().getCloudServiceByName(context.getArgument("service", String.class)))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("shutdown")
                                        .executes(context -> serviceShutdown(context, TeriumAPI.getTeriumAPI().getServiceManager().getCloudServiceByName(context.getArgument("service", String.class)))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("lock")
                                        .executes(context -> lockService(context, TeriumAPI.getTeriumAPI().getServiceManager().getCloudServiceByName(context.getArgument("service", String.class)))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("unlock")
                                        .executes(context -> unlockService(context, TeriumAPI.getTeriumAPI().getServiceManager().getCloudServiceByName(context.getArgument("service", String.class)))))))
                // Group
                .then(LiteralArgumentBuilder.<CommandSource>literal("group")
                        .executes(this::sendGroupHelp)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("group", StringArgumentType.string())
                                .suggests(this::suggestGroups)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("info")
                                        .executes(context -> groupInfo(context, TeriumAPI.getTeriumAPI().getServiceGroupManager().getServiceGroupByName(context.getArgument("group", String.class)))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("shutdown")
                                        .executes(context -> groupShutdown(context, TeriumAPI.getTeriumAPI().getServiceGroupManager().getServiceGroupByName(context.getArgument("group", String.class)))))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("start")
                                        .executes(context -> startService(context, TeriumAPI.getTeriumAPI().getServiceGroupManager().getServiceGroupByName(context.getArgument("group", String.class)))))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> service <service> info<gray>|<white>shutdown<gray>|<white>lock<gray>|<white>unlock"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> group <name> info<gray>|<white>shutdown<gray>|<white>update<gray>|<white>start"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> list<gray>|<white>modules"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> exit"));
        return 1;
    }

    // Service stuff

    private int sendServiceHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> service <service> info<gray>|<white>screen<gray>|<white>shutdown"));
        return 1;
    }

    private CompletableFuture<Suggestions> suggestServices(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getServiceManager().getAllCloudServices().forEach(iCloudService -> suggestionsBuilder.suggest(iCloudService.getServiceName()));
        return suggestionsBuilder.buildFuture();
    }

    private int serviceInfo(CommandContext<CommandSource> context, ICloudService iCloudService) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Name: " + iCloudService.getServiceName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Id: #" + (iCloudService.getServiceId() < 10 ? "0" + iCloudService.getServiceId() : iCloudService.getServiceId())));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>State: " + iCloudService.getServiceState()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Group: " + iCloudService.getServiceGroup().getServiceGroupName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Type: " + iCloudService.getServiceType()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Locked: " + iCloudService.isLocked()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Used memory: " + iCloudService.getUsedMemory()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Max memory: " + iCloudService.getMaxMemory()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Online players: " + iCloudService.getOnlinePlayers()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Max players: " + iCloudService.getMaxPlayers()));
        return 1;
    }

    private int serviceShutdown(CommandContext<CommandSource> context, ICloudService iCloudService) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Successfully stopped " + iCloudService.getServiceName()));
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceShutdown(iCloudService.getServiceName()));
        return 1;
    }

    private int lockService(CommandContext<CommandSource> context, ICloudService iCloudService) {
        if(iCloudService.isLocked()) {
            context.getSource().sendMessage(Component.text("§cThis service is already locked!"));
            return 1;
        }

        iCloudService.setLocked(true);
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceLock(iCloudService.getServiceName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Successfully locked service " + iCloudService.getServiceName()));
        return 1;
    }

    private int unlockService(CommandContext<CommandSource> context, ICloudService iCloudService) {
        if(!iCloudService.isLocked()) {
            context.getSource().sendMessage(Component.text("§cThis service isn't locked yet!"));
            return 1;
        }

        iCloudService.setLocked(false);
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceUnlock(iCloudService.getServiceName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Successfully unlocked service " + iCloudService.getServiceName()));
        return 1;
    }

    // Group stuff

    private int sendGroupHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>/cloud</gradient> group <name> info<gray>|<white>shutdown<gray>|<white>update<gray>|<white>start"));
        return 1;
    }

    private CompletableFuture<Suggestions> suggestGroups(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getServiceGroupManager().getAllServiceGroups().forEach(iCloudServiceGroup -> suggestionsBuilder.suggest(iCloudServiceGroup.getServiceGroupName()));
        return suggestionsBuilder.buildFuture();
    }

    private int groupInfo(CommandContext<CommandSource> context, ICloudServiceGroup iCloudServiceGroup) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Name: " + iCloudServiceGroup.getServiceGroupName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Title: " + iCloudServiceGroup.getGroupTitle()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Type: " + iCloudServiceGroup.getServiceType()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Node: " + iCloudServiceGroup.getServiceGroupNode()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Maintenance: " + iCloudServiceGroup.isMaintenance()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Version: " + iCloudServiceGroup.getVersion()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Max memory: " + iCloudServiceGroup.getMemory()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Max players: " + iCloudServiceGroup.getMaximumPlayers()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Minimal services: " + iCloudServiceGroup.getMinimalServices()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <white>Maximal services: " + iCloudServiceGroup.getMaximalServices()));
        return 1;
    }

    private int groupShutdown(CommandContext<CommandSource> context, ICloudServiceGroup iCloudServiceGroup) {
        TeriumAPI.getTeriumAPI().getServiceManager().getCloudServicesByGroupName(iCloudServiceGroup.getServiceGroupName()).forEach(iCloudService -> TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceShutdown(iCloudService.getServiceName())));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Successfully shutdowned all services from group " + iCloudServiceGroup.getServiceGroupName()));
        return 1;
    }

    private int startService(CommandContext<CommandSource> context, ICloudServiceGroup iCloudServiceGroup) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceStart(iCloudServiceGroup.getServiceGroupName()));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("Information was send to the cloud. (starting a new service from servicegroup '" + iCloudServiceGroup.getServiceGroupName() + "')"));
        return 1;
    }
}