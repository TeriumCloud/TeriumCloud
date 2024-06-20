package cloud.terium.plugin.velocity.command;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class CloudCommand {

    public String name;

    public BrigadierCommand build(String name) {
        this.name = name;
        LiteralCommandNode<CommandSource> literalCommand = LiteralArgumentBuilder.<CommandSource>literal(name)
                .executes(this::help)
                .then(LiteralArgumentBuilder.<CommandSource>literal("list")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.list") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::list))
                .then(LiteralArgumentBuilder.<CommandSource>literal("modules")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.modules") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::modules))
                .then(LiteralArgumentBuilder.<CommandSource>literal("groups")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.groups") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::groups))
                .then(LiteralArgumentBuilder.<CommandSource>literal("service")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.service") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                .executes(this::serviceInfo)
                                .suggests((context, builder) -> {
                                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(cloudService -> builder.suggest(cloudService.getServiceName()));
                                    return builder.buildFuture();
                                })
                                .then(LiteralArgumentBuilder.<CommandSource>literal("shutdown")
                                        .executes(this::shutdownService))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("stop")
                                        .executes(this::shutdownService))
                        ))
                .then(LiteralArgumentBuilder.<CommandSource>literal("player")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.player") || commandSource.hasPermission("terium.command.*"))
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
                .then(LiteralArgumentBuilder.<CommandSource>literal("start")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.service") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("group", StringArgumentType.string())
                                .suggests(this::groupSuggestion)
                                .executes(this::help)
                                .then(RequiredArgumentBuilder.<CommandSource, Integer>argument("count", IntegerArgumentType.integer())
                                        .suggests((context, builder) -> builder.suggest(1).suggest(2).buildFuture())
                                        .executes(this::startService))))
                .then(LiteralArgumentBuilder.<CommandSource>literal("copy")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.service") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::help)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("service", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(cloudService -> builder.suggest(cloudService.getServiceName()));
                                    return builder.buildFuture();
                                })
                                .executes(this::help)
                                .then(RequiredArgumentBuilder.<CommandSource, String>argument("template", StringArgumentType.string())
                                        .suggests(this::templateSuggestion)
                                        .executes(this::copyService))))
                .then(LiteralArgumentBuilder.<CommandSource>literal("stopUselessServices")
                        .requires(commandSource -> commandSource.hasPermission("terium.command.stopUselessServices") || commandSource.hasPermission("terium.command.*"))
                        .executes(this::stopUselessServices))
                .build();

        return new BrigadierCommand(literalCommand);
    }

    private int help(CommandContext<CommandSource> context) {
        if (!context.getSource().hasPermission("terium.command.service") || !context.getSource().hasPermission("terium.command.player") || !context.getSource().hasPermission("terium.command.*")
                || !context.getSource().hasPermission("terium.command.groups") || !context.getSource().hasPermission("terium.command.modules") || !context.getSource().hasPermission("terium.command.list")) {
            context.getSource().sendMessage(Component.text("§cYou don't have enought permissions to execute this command!"));
            return 1;
        }

        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>terium-cloud</gradient> v" + TeriumAPI.getTeriumAPI().getProvider().getVersion()));
        context.getSource().sendMessage(Component.text(" "));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " list"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " modules"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " groups"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " stopUselessServices"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " start <group>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " copy <service> <template>"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " service <service> shutdown|stop"));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + name + " player <player> kick|connect (service)"));
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

    private CompletableFuture<Suggestions> groupSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(cloudService -> suggestionsBuilder.suggest(cloudService.getGroupName()));
        return suggestionsBuilder.buildFuture();
    }

    private CompletableFuture<Suggestions> templateSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates().forEach(template -> suggestionsBuilder.suggest(template.getName()));
        return suggestionsBuilder.buildFuture();
    }

    private int shutdownService(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).ifPresent(ICloudService::shutdown);
        return 1;
    }

    private int serviceInfo(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).ifPresentOrElse(cloudService -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Information about '" + cloudService.getServiceName() + "':"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<gray>● <aqua>" + cloudService.getServiceName() + "<white>:"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>ID: #" + cloudService.getServiceId() + " <gray>| <white>State: " + "<" + cloudService.getServiceState().getHex() + ">" + cloudService.getServiceState()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Type: <#87a19c>" + cloudService.getServiceType() + " <gray>| <white>Templates: " + cloudService.getTemplates().stream().map(ITemplate::getName).toList()));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Host: <red>" + cloudService.getServiceGroup().getGroupNode().getAddress().getAddress().getHostAddress() + ":" + cloudService.getPort() + " <gray>| <white>Players: <#a7c7d6>" + cloudService.getOnlinePlayers() + "<white>/<#a79ed9>" + cloudService.getMaxPlayers() + "<white>"));
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Memory: " + cloudService.getUsedMemory() + "/" + cloudService.getMaxMemory()));
            if (cloudService.getPropertyMap() == null || cloudService.getPropertyMap().keySet().isEmpty()) {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Properties: <red>None"));
            } else {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("  <gray>● <white>Properties: <green>" + cloudService.getPropertyMap().keySet().size()));
                cloudService.getPropertyMap().keySet().forEach(s -> {
                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("     <gray>● <white>" + s + ": <yellow>" + cloudService.getPropertyMap().get(s)));
                });
            }
        }, () -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>A service with that name isn't registered.")));
        return 1;
    }

    private int kickPlayer(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(context.getArgument("player", String.class)).flatMap(cloudPlayer -> TeriumVelocityStartup.getInstance().getProxyServer().getPlayer(cloudPlayer.getUniqueId())).ifPresent(player -> player.disconnect(Component.text("§cYou got kicked.")));
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<green>Successfully kicked " + context.getArgument("player", String.class)));
        return 1;
    }

    private int sendPlayer(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(context.getArgument("player", String.class)).ifPresent(cloudPlayer -> cloudPlayer.connectWithService(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).orElseGet(null)));
        return 1;
    }

    private int list(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(Component.text(" "));
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Services from group '<#96908c>" + group.getGroupName() + "<white>':"));
            TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(group.getGroupName()).forEach(service ->
                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Name: <#c49b9b>" + service.getServiceName() + "<white> | State: " + "<" + service.getServiceState().getHex() + ">" + service.getServiceState() + "<white> | Players: <#a7c7d6>" + service.getOnlinePlayers() + "<white>/<#a79ed9>" + service.getMaxPlayers() + "<white>")));

            context.getSource().sendMessage(Component.text(" "));
        });
        return 1;
    }

    private int modules(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().forEach(module -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + module.getName() + "(<#96908c>" + module.getFileName() + "<white>) by <#87a19c>" + module.getAuthor() + "<white> version <#87a19c>" + module.getVersion() + "<white>."));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There are no loaded modules."));
        return 1;
    }

    private int groups(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(serviceGroup -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Name: " + serviceGroup.getGroupName() + "(" + serviceGroup.getServiceType().toString().toUpperCase() + ") - Online services: " + TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getOnlineServicesFromServiceGroup(serviceGroup.getGroupName()) + " - Templates: " + serviceGroup.getTemplates().stream().map(ITemplate::getName).toList()));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There are no loaded service groups."));
        return 1;
    }

    private int players(CommandContext<CommandSource> context) {
        if (TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getOnlinePlayers().forEach(player -> {
                context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + player.getUsername() + "(on <#91d177>" + player.getConnectedCloudService().orElseGet(null).getServiceName() + "<white>)")
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§7Connect with " + player.getUsername() + "'s service.").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + player.getConnectedCloudService().orElseGet(null).getServiceName())))));
            });
        else
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There are no players online."));
        return 1;
    }

    private int startService(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(context.getArgument("group", String.class)).ifPresentOrElse(serviceGroup -> {
            for (int i = 0; i < context.getArgument("count", Integer.class); i++) {
                TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().createService(serviceGroup);
            }
            context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Trying to start " + (context.getArgument("count", Integer.class) == 1 ? "one" : context.getArgument("count", Integer.class)) + " new service of group <gray>'<#00d4ff>" + serviceGroup.getGroupName() + "<gray>'."));
        }, () -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There is no service group with that name.")));
        return 1;
    }

    private int copyService(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(context.getArgument("service", String.class)).ifPresentOrElse(cloudService -> {
            TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(context.getArgument("template", String.class)).ifPresentOrElse(cloudService::copy, () -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There is no template with that name.")));
        }, () -> context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "<red>There is no service group with that name.")));
        return 1;
    }

    private int stopUselessServices(CommandContext<CommandSource> context) {
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
            if (TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(group.getGroupName()).size() > group.getMinServices()) {
                TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(group.getGroupName()).stream().filter(cloudService -> cloudService.getServiceState().equals(ServiceState.ONLINE) && cloudService.getOnlinePlayers() == 0).sorted(Comparator.comparing(ICloudService::getServiceId).reversed()).findFirst().ifPresent(ICloudService::shutdown);
            }
        });
        context.getSource().sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Stopping all services from all service groups with enought online services<gray>."));
        return 1;
    }
}
