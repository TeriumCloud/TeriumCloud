package cloud.terium.plugin.bungeecord.command;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.plugin.bungeecord.TeriumBungeecordStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import cloud.terium.teriumapi.template.ITemplate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CloudCommand extends Command implements TabExecutor {

    private final BungeeAudiences audiences = BungeeAudiences.builder(TeriumBungeecordStartup.getInstance()).build();

    public CloudCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list" -> {
                if (sender.hasPermission("terium.command.list") || sender.hasPermission("terium.command.*")) {
                    list(sender);
                } else {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                }
            }
            case "modules" -> {
                if (sender.hasPermission("terium.command.modules") || sender.hasPermission("terium.command.*")) {
                    modules(sender);
                } else {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                }
            }
            case "groups" -> {
                if (sender.hasPermission("terium.command.groups") || sender.hasPermission("terium.command.*")) {
                    groups(sender);
                } else {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                }
            }
            case "service" -> {
                if(sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*")) {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                    return;
                }

                if (args.length > 1) {
                    serviceCommand(sender, args);
                } else {
                    sender.sendMessage(new TextComponent("§cUsage: /" + getName() + " service <service> [shutdown|stop]"));
                }
            }
            case "player" -> {
                if(sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*")) {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                    return;
                }

                if (args.length > 1) {
                    playerCommand(sender, args);
                } else {
                    sender.sendMessage(new TextComponent("§cUsage: /" + getName() + " player <player> [kick|connect <service>]"));
                }
            }
            case "start" -> {
                if(sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*")) {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                    return;
                }

                if (args.length > 2) {
                    startService(sender, args);
                } else {
                    sender.sendMessage(new TextComponent("§cUsage: /" + getName() + " start <group> <count>"));
                }
            }
            case "copy" -> {
                if(sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*")) {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                    return;
                }

                if (args.length > 2) {
                    copyService(sender, args);
                } else {
                    sender.sendMessage(new TextComponent("§cUsage: /" + getName() + " copy <service> <template>"));
                }
            }
            case "stopuselessservices" -> {
                if (sender.hasPermission("terium.command.stopUselessServices") || sender.hasPermission("terium.command.*")) {
                    stopUselessServices(sender);
                } else {
                    sender.sendMessage(new TextComponent("§cYou don't have permission to execute this command!"));
                }
            }
            default -> help(sender);
        }
    }

    private void help(CommandSender sender) {
        if (!sender.hasPermission("terium.command.service") || !sender.hasPermission("terium.command.player") || !sender.hasPermission("terium.command.*")
                || !sender.hasPermission("terium.command.groups") || !sender.hasPermission("terium.command.modules") || !sender.hasPermission("terium.command.list")) {
            audience(sender).sendMessage(Component.text("§cYou don't have enought permissions to execute this command!"));
            return;
        }

        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#245dec:#00d4ff>terium-cloud</gradient> v" + TeriumAPI.getTeriumAPI().getProvider().getVersion()));
        audience(sender).sendMessage(Component.text(" "));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " list"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " modules"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " groups"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " stopUselessServices"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " start <group>"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " copy <service> <template>"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " service <service> shutdown|stop"));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "/" + getName() + " player <player> kick|connect (service)"));
    }

    private void list(CommandSender sender) {
        Audience audience = (sender instanceof ProxiedPlayer) ? audiences.player((ProxiedPlayer) sender) : Audience.audience(audiences.sender(sender));
        sender.sendMessage(new TextComponent(" "));
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(group -> {
            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + "Services from group '<#96908c>" + group.getGroupName() + "<white>':"));
            TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServicesByGroupName(group.getGroupName()).forEach(service ->
                    audience(sender).sendMessage(MiniMessage.miniMessage().deserialize(TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Name: <#c49b9b>" + service.getServiceName() + "<white> | State: " + "<" + service.getServiceState().getHex() + ">" + service.getServiceState() + "<white> | Players: <#a7c7d6>" + service.getOnlinePlayers() + "<white>/<#a79ed9>" + service.getMaxPlayers() + "<white>")));

            sender.sendMessage(new TextComponent(" "));
        });
    }

    private Audience audience(CommandSender sender) {
        return (sender instanceof ProxiedPlayer) ? audiences.player((ProxiedPlayer) sender) : Audience.audience(audiences.sender(sender));
    }

    private void modules(CommandSender sender) {
        if (TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getModuleProvider().getAllModules().forEach(module -> {
                audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + module.getName() + "(<#96908c>" + module.getFileName() + "<white>) by <#87a19c>" + module.getAuthor() + "<white> version <#87a19c>" + module.getVersion() + "<white>.")));
            });
        else
            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>There are no loaded modules.")));
    }

    private void groups(CommandSender sender) {
        if (TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().size() > 0)
            TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(serviceGroup -> {
                audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "Name: " + serviceGroup.getGroupName() + "(" + serviceGroup.getServiceType().toString().toUpperCase() + ") - Online services: " + TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getOnlineServicesFromServiceGroup(serviceGroup.getGroupName()) + " - Templates: " + serviceGroup.getTemplates().stream().map(ITemplate::getName).collect(Collectors.joining(", ")))));
            });
        else
            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>There are no loaded service groups.")));
    }

    private void serviceCommand(CommandSender sender, String[] args) {
        String serviceName = args[1];
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(serviceName).ifPresentOrElse(service -> {
            if (args.length > 2) {
                switch (args[2].toLowerCase()) {
                    case "shutdown":
                    case "stop":
                        service.shutdown();
                        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<green>Service '" + serviceName + "' has been shut down.")));
                        break;
                }
            } else {
                serviceInfo(sender, service);
            }
        }, () -> audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>A service with that name isn't registered."))));
    }

    private void serviceInfo(CommandSender sender, ICloudService service) {
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "Information about '" + service.getServiceName() + "':")));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + " <gray>● <white>State: " + "<" + service.getServiceState().getHex() + ">" + service.getServiceState() + "<white>")));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Group: <#c49b9b>" + service.getServiceGroup().getGroupName() + "<white>")));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Type: <#c49b9b>" + service.getServiceGroup().getServiceType().name() + "<white>")));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Players: <#a7c7d6>" + service.getOnlinePlayers() + "<white>/<#a79ed9>" + service.getMaxPlayers() + "<white>")));
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + " <gray>● <white>Host: <#c49b9b>" + service.getServiceGroup().getGroupNode().getAddress().getAddress().getHostAddress() + ":" + service.getPort() + "<white>")));
    }

    private void playerCommand(CommandSender sender, String[] args) {
        String playerName = args[1];
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

        if (player != null) {
            if (args.length > 2) {
                switch (args[2].toLowerCase()) {
                    case "kick" -> {
                        player.disconnect(new TextComponent("§cYou got kicked by " + sender.getName() + "."));
                        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<green>Player '" + playerName + "' has been kicked.")));
                    }
                    case "connect" -> {
                        if (args.length > 3) {
                            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(playerName).ifPresent(cloudPlayer -> cloudPlayer.connectWithService(TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(args[3]).orElseGet(null)));
                        } else {
                            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>Usage: /" + getName() + " player <player> connect <service>")));
                        }
                    }
                    default ->
                            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>Invalid player command. Use kick or connect.")));
                }
            } else {
                audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>Usage: /" + getName() + " player <player> [kick|connect <service>]")));
            }
        } else {
            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>A player with that name isn't online.")));
        }
    }

    private void startService(CommandSender sender, String[] args) {
        String groupName = args[1];
        int count = Integer.parseInt(args[2]);
        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getServiceGroupByName(groupName).ifPresentOrElse(group -> {
            for (int i = 0; i < count; i++) {
                TeriumAPI.getTeriumAPI().getFactory().getServiceFactory().createService(group);
            }
            audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<green>Started " + count + " services from group '" + groupName + "'.")));
        }, () -> audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>A group with that name isn't registered."))));
    }

    private void copyService(CommandSender sender, String[] args) {
        String serviceName = args[1];
        String templateName = args[2];
        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(serviceName).ifPresentOrElse(service -> {
            TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(templateName).ifPresentOrElse(template -> {
                service.copy(template);
                audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<green>Service '" + serviceName + "' has been copied to template '" + templateName + "'.")));
            }, () -> audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>A template with that name isn't registered."))));
        }, () -> audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<red>A service with that name isn't registered."))));
    }

    private void stopUselessServices(CommandSender sender) {
        List<ICloudService> servicesToStop = TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream()
                .filter(service -> service.getServiceState() == ServiceState.ONLINE && service.getServiceGroup().getServiceType() != ServiceType.Lobby && service.getServiceGroup().getServiceType() != ServiceType.Proxy && service.getOnlinePlayers() == 0)
                .sorted(Comparator.comparing(ICloudService::getServiceName))
                .collect(Collectors.toList());

        servicesToStop.forEach(ICloudService::shutdown);
        audience(sender).sendMessage(MiniMessage.miniMessage().deserialize((TeriumExtension.getInstance().getPrefix() + "<green>Stopped " + servicesToStop.size() + " useless services.")));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("terium.command.list") || sender.hasPermission("terium.command.*"))
                suggestions.add("list");
            if (sender.hasPermission("terium.command.modules") || sender.hasPermission("terium.command.*"))
                suggestions.add("modules");
            if (sender.hasPermission("terium.command.groups") || sender.hasPermission("terium.command.*"))
                suggestions.add("groups");
            if (sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*"))
                suggestions.add("service");
            if (sender.hasPermission("terium.command.player") || sender.hasPermission("terium.command.*"))
                suggestions.add("player");
            if (sender.hasPermission("terium.command.start") || sender.hasPermission("terium.command.*"))
                suggestions.add("start");
            if (sender.hasPermission("terium.command.copy") || sender.hasPermission("terium.command.*"))
                suggestions.add("copy");
            if (sender.hasPermission("terium.command.stopUselessServices") || sender.hasPermission("terium.command.*"))
                suggestions.add("stopUselessServices");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "service" -> {
                    if (sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*"))
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(service -> suggestions.add(service.getServiceName()));
                }
                case "player" -> {
                    if (sender.hasPermission("terium.command.player") || sender.hasPermission("terium.command.*"))
                        ProxyServer.getInstance().getPlayers().forEach(player -> suggestions.add(player.getName()));
                }
                case "start" -> {
                    if (sender.hasPermission("terium.command.start") || sender.hasPermission("terium.command.*"))
                        TeriumAPI.getTeriumAPI().getProvider().getServiceGroupProvider().getAllServiceGroups().forEach(group -> suggestions.add(group.getGroupName()));
                }
                case "copy" -> {
                    if (sender.hasPermission("terium.command.copy") || sender.hasPermission("terium.command.*"))
                        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().forEach(service -> suggestions.add(service.getServiceName()));
                }
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "service" -> {
                    if (sender.hasPermission("terium.command.service") || sender.hasPermission("terium.command.*")) {
                        suggestions.add("shutdown");
                        suggestions.add("stop");
                    }
                }
                case "player" -> {
                    if (sender.hasPermission("terium.command.player") || sender.hasPermission("terium.command.*")) {
                        suggestions.add("kick");
                        suggestions.add("connect");
                    }
                }
                case "copy" -> {
                    if (sender.hasPermission("terium.command.copy") || sender.hasPermission("terium.command.*"))
                        TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates().forEach(template -> suggestions.add(template.getName()));
                }
            }
        }
        return suggestions.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }
}
