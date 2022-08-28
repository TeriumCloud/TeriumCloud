package cloud.terium.bridge.velocity.commands;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.bridge.player.CloudPlayer;
import cloud.terium.bridge.velocity.BridgeVelocityStartup;
import cloud.terium.teriumapi.service.CloudServiceType;
import cloud.terium.teriumapi.service.ICloudService;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.ThreadLocalRandom;

public class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        CloudPlayer cloudPlayer = TeriumBridge.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUsername(), player.getUniqueId());

        if (!cloudPlayer.getConnectedService().getServiceGroup().getServiceType().equals(CloudServiceType.Lobby)) {
            if (!TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().isEmpty()) {
                if (TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().stream().filter(ICloudService::isOnline).toList().size() > 1) {
                    player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().get(ThreadLocalRandom.current().nextInt(1, TeriumBridge.getInstance().getServiceManager().getAllCloudServices().size())).getServiceName()).get()).connect();
                    player.sendMessage(MiniMessage.miniMessage().deserialize(TeriumBridge.getInstance().getPrefix() + "The cloudsystem is trying to connect you with a lobby service."));
                } else {
                    ICloudService service = TeriumBridge.getInstance().getServiceManager().getAllLobbyServices().get(0);

                    if (service.isOnline()) {
                        player.createConnectionRequest(BridgeVelocityStartup.getInstance().getProxyServer().getServer(service.getServiceName()).get()).connect();
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
