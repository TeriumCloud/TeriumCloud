package cloud.terium.module.hubcommand.command;

import cloud.terium.module.hubcommand.velocity.HubCommandStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.service.ServiceState;
import cloud.terium.teriumapi.service.ServiceType;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Comparator;

public class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        ICloudPlayer cloudPlayer = TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(((Player) invocation.source()).getUniqueId()).orElseGet(null);

        if (cloudPlayer.getConnectedCloudService().get().getServiceType().equals(ServiceType.Lobby)) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(HubCommandStartup.getInstance().getConfigManager().getJson().get("already.message").getAsString()));
            return;
        }

        TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllLobbyServices().stream()
                .filter(service -> service.getServiceState().equals(ServiceState.ONLINE))
                .filter(service -> !service.isLocked())
                .filter(service -> (player.getCurrentServer().isEmpty()
                        || !player.getCurrentServer().get().getServerInfo().getName().equals(service.getServiceName())))
                .min(Comparator.comparing(ICloudService::getOnlinePlayers)).ifPresentOrElse(cloudService -> {
                    cloudPlayer.connectWithService(cloudService);
                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(HubCommandStartup.getInstance().getConfigManager().getJson().get("successful.message").getAsString()));
                }, () -> player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(HubCommandStartup.getInstance().getConfigManager().getJson().get("unavailable.message").getAsString())));
    }
}