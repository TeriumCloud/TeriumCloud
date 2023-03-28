package cloud.terium.plugin.velocity.command;

import cloud.terium.plugin.TeriumPlugin;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
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
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.CompletableFuture;

public class CloudCommand {

    public String name;

    public BrigadierCommand build(String name) {
        this.name = name;
        LiteralCommandNode<CommandSource> literalCommand = LiteralArgumentBuilder.<CommandSource>literal(name)
                .executes(this::help)
                .then(LiteralArgumentBuilder.<CommandSource>literal("list")
                        .requires(commandSource -> commandSource.hasPermission("terium.command"))
                        .executes(this::list))
                .then(LiteralArgumentBuilder.<CommandSource>literal("modules")
                        .requires(commandSource -> commandSource.hasPermission("terium.command"))
                        .executes(this::modules))
                .then(LiteralArgumentBuilder.<CommandSource>literal("groups")
                        .requires(commandSource -> commandSource.hasPermission("terium.command"))
                        .executes(this::groups))
                .then(LiteralArgumentBuilder.<CommandSource>literal("service")
                        .requires(commandSource -> commandSource.hasPermission("terium.command"))
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                .executes(this::help)
                                .suggests(this::serviceAllSuggestion)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("shutdown")
                                        .executes(this::shutdownService))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("stop")
                                        .executes(this::shutdownService))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("info")
                                        .executes(this::serviceInfo))))
                .then(LiteralArgumentBuilder.<CommandSource>literal("player")
                        .requires(commandSource -> commandSource.hasPermission("terium.command"))
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("player", StringArgumentType.string())
                                .executes(this::help)
                                .suggests(this::playerSuggestion)
                                .then(LiteralArgumentBuilder.<CommandSource>literal("kick")
                                        .executes(this::kickPlayer))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("connect")
                                        .executes(this::help)
                                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                                .suggests(this::serviceSuggestion)
                                                .executes(this::sendPlayer))))
                        .executes(this::players))
                .build();

        return new BrigadierCommand(literalCommand);
    }

    private int help(CommandContext<CommandSource> context) {
        if(!context.getSource().hasPermission("terium.command")) {
            context.getSource().sendMessage(Component.text("§cYou don't have enought permissions to execute this command!"));
            return 1;
        }

        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>terium-cloud</gradient> v" + TeriumAPI.getTeriumAPI().getProvider().getVersion()));
        context.getSource().sendMessage(Component.text(" "));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "/" + name + " list"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "/" + name + " modules"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "/" + name + " groups"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "/" + name + " service <service> shutdown|stop|info"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "/" + name + " player <player> kick|connect (service)"));
        return 1;
    }

    private CompletableFuture<Suggestions> playerSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().forEach(player -> suggestionsBuilder.suggest(player.getUsername()));
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> serviceSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream().filter(cloudService -> cloudService.getServiceType() != ServiceType.Proxy).forEach(cloudService -> suggestionsBuilder.suggest(cloudService.getServiceName()));
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> serviceAllSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(cloudService -> suggestionsBuilder.suggest(cloudService.getServiceName()));
        return suggestionsBuilder.buildFuture();
    }

    private int shutdownService(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).ifPresent(ICloudService::shutdown);
        return 1;
    }

    private int serviceInfo(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).ifPresentOrElse(cloudService -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Information about '" + cloudService.getServiceName() + "':"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Name: " + cloudService.getServiceName()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Id: " + cloudService.getServiceId()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Group: " + cloudService.getServiceGroup().getGroupName()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Type: " + cloudService.getServiceType()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "State: " + cloudService.getServiceState()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Node: " + cloudService.getServiceNode().getName()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Players: " + cloudService.getOnlinePlayers() + "/" + cloudService.getMaxPlayers()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Memory: " + cloudService.getUsedMemory() + "/" + cloudService.getMaxMemory()));
        }, () -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "<red>A service with that name isn't registered.")));
        return 1;
    }

    private int kickPlayer(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(context.getArgument("player", String.class)).flatMap(cloudPlayer -> TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(cloudPlayer.getUniqueId())).ifPresent(player -> player.disconnect(Component.text("§cYou got kicked.")));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "<green>Successfully kicked " + context.getArgument("player", String.class)));
        return 1;
    }

    private int sendPlayer(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(context.getArgument("player", String.class)).ifPresent(cloudPlayer -> cloudPlayer.connectWithService(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).orElseGet(null)));
        return 1;
    }

    private int list(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(Component.text(" "));
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Services from group '<#96908c>" + group.getGroupName() + "<white>':"));
            TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(group.getGroupName()).forEach(service ->
                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "- Name: <#c49b9b>" + service.getServiceName() + "<white> | State: " + "<" + service.getServiceState().getHex() + ">" + service.getServiceState() + "<white> | Players: <#a7c7d6>" + service.getOnlinePlayers() + "<white>/<#a79ed9>" + service.getMaxPlayers() + "<white>")));

            context.getSource().sendMessage(Component.text(" "));
        });
        return 1;
    }

    private int modules(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().forEach(module -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + module.getName() + "(<#96908c>" + module.getFileName() + "<white>) by <#87a19c>" + module.getAuthor() + "<white> version <#87a19c>" + module.getVersion() + "<white>."));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "<red>There are no loaded modules."));
        return 1;
    }

    private int groups(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(serviceGroup -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "Name: " + serviceGroup.getGroupName() + "(" + serviceGroup.getServiceType().toString().toUpperCase() + ") - Online services: " + TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getOnlineServicesFromServiceGroup(serviceGroup.getGroupName()) + " - Templates: " + serviceGroup.getTemplates().stream().map(ITemplate::getName).toList()));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "<red>There are no loaded service groups."));
        return 1;
    }

    private int players(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().forEach(player -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + player.getUsername() + "(on <#91d177>" + player.getConnectedCloudService().orElseGet(null).getServiceName() + "<white>)")
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§7Connect with " + player.getUsername() + "'s service.").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + player.getConnectedCloudService().orElseGet(null).getServiceName())))));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumPlugin.getInstance().getPrefix() + "<red>There are no players online."));
        return 1;
    }
}
