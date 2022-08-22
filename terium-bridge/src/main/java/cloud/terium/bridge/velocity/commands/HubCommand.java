package cloud.terium.bridge.velocity.commands;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.cloudsystem.service.MinecraftService;
import cloud.terium.cloudsystem.service.ServiceType;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.ThreadLocalRandom;

public class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId());

        if (!cloudPlayer.getConnectedService().getDefaultServiceGroup().serviceType().equals(ServiceType.Lobby)) {
            if (!TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().isEmpty()) {
                if (TeriumBridge.getInstance().getServiceManager().getLobbyServices().stream().filter(MinecraftService::online).toList().size() > 1) {
                    player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(TeriumBridge.getInstance().getServiceManager().getLobbyServices().get(ThreadLocalRandom.current().nextInt(1, TeriumBridge.getInstance().getServiceManager().getLobbyServices().size())).serviceName()).get()).connect();
                    player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The cloudsystem is trying to connect you with a lobby service."));
                } else {
                    MinecraftService service = TeriumBridge.getInstance().getServiceManager().getLobbyServices().get(0);

                    if (service.online()) {
                        player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(service.serviceName()).get()).connect();
                        player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The cloudsystem is trying to connect you with a lobby service."));
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#ef7b7b>The lobby service, you trying to connect to is in starting mode!"));
                    }
                }
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#ef7b7b>The cloudsystem can't find a lobby service."));
            }
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "<#ef7b7b>You are already connected with a lobby service!"));
        }
    }
}
