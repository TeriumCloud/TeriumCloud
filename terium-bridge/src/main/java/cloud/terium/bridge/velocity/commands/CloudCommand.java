package cloud.terium.bridge.velocity.commands;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.networking.packets.PacketPlayOutServiceShutdown;
import cloud.terium.networking.packets.PacketPlayOutServiceStart;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("cloud").requires(commandSource -> TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(((Player) commandSource).getUsername(), ((Player) commandSource).getUniqueId()).isAdmin())
                .executes(this::sendHelp)
                .then(LiteralArgumentBuilder.<CommandSource>literal("list").executes(this::sendServerList))
                .then(LiteralArgumentBuilder.<CommandSource>literal("start").executes(this::sendHelp)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("servicegroup", StringArgumentType.string())
                                .suggests(this::suggestServiceGroups)
                                .executes(this::startService)))
                .then(LiteralArgumentBuilder.<CommandSource>literal("shutdown").executes(this::sendHelp)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                .suggests(this::suggestOnlineServices)
                                .executes(this::shutdownService)))
                .build();
        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<gradient:#245dec:#00d4ff>Terium</gradient> <white>by Jxnnik v1.0.0-SNAPSHOT"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#00d4ff><bold>/</bold><white>cloud list"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#00d4ff><bold>/</bold><white>cloud start <service_group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#00d4ff><bold>/</bold><white>cloud shutdown <service_name>"));
        return 1;
    }

    private int sendServerList(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "List of all services: "));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" "));
        TeriumBridge.getInstance().getServiceGroupManager().getServiceGroups().forEach(group -> {
            AtomicInteger i = new AtomicInteger();
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + group.getServiceGroupName() + " (Parent: " + group.getGroupTitle() + " / Type: " + group.getServiceType() + ")"));
            TeriumBridge.getInstance().getServiceManager().getMinecraftServices().stream().filter(service -> service.getServiceGroup().equals(group)).forEach(service -> {
                i.getAndIncrement();
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + i.get() + ". <gradient:#245dec:#00d4ff>" + service.getServiceName() + "</gradient> / Port: " + service.getPort() + " / State: " + service.isOnline()));
            });
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(" "));
        });
        return 1;
    }

    private int startService(CommandContext<CommandSource> context) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceStart(TeriumBridge.getInstance().getServiceGroupManager().getServiceGroupByName(context.getArgument("servicegroup", String.class)).getServiceGroupName()));
        return 1;
    }

    private int shutdownService(CommandContext<CommandSource> context) {
        TeriumBridge.getInstance().getTeriumNetworkListener().getDefaultTeriumNetworking().sendPacket(new PacketPlayOutServiceShutdown(TeriumBridge.getInstance().getServiceManager().getCloudServiceByName(context.getArgument("service", String.class)).getServiceName()));
        return 1;
    }

    private CompletableFuture<Suggestions> suggestServiceGroups(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumBridge.getInstance().getServiceGroupManager().getServiceGroups().forEach(group -> {
            suggestionsBuilder.suggest(group.getServiceGroupName());
        });
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOnlineServices(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumBridge.getInstance().getServiceManager().getMinecraftServices().forEach(service -> {
            suggestionsBuilder.suggest(service.getServiceName());
        });
        return suggestionsBuilder.buildFuture();
    }
}