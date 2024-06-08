package cloud.terium.module.hubcommand.bungeecord.command;

import cloud.terium.module.hubcommand.velocity.HubCommandVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Comparator;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        ICloudPlayer cloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(player.getUniqueId()).orElseGet(null);

        if (cloudPlayer.getConnectedCloudService().get().getServiceType().equals(ServiceType.Lobby)) {
            player.sendMessage(HubCommandVelocityStartup.getInstance().getConfigManager().getJson().get("already.message").getAsString());
            return;
        }

        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllLobbyServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.isLocked())
                .filter(service -> !player.getServer().getInfo().getName().equals(service.getServiceName()))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers)).ifPresentOrElse(cloudService -> {
                    cloudPlayer.connectWithService(cloudService);
                    player.sendMessage(HubCommandVelocityStartup.getInstance().getConfigManager().getJson().get("successful.message").getAsString());
                }, () -> player.sendMessage(HubCommandVelocityStartup.getInstance().getConfigManager().getJson().get("unavailable.message").getAsString()));
    }
}
