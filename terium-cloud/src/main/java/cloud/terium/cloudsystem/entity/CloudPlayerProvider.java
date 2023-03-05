package cloud.terium.cloudsystem.entity;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerAdd;
import cloud.terium.networking.packet.player.PacketPlayOutCloudPlayerRegister;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import cloud.terium.teriumapi.player.impl.CloudPlayer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CloudPlayerProvider implements ICloudPlayerProvider {

    private final List<ICloudPlayer> onlinePlayers = new ArrayList<>();

    public void registerPlayer(String username, UUID uniquedId, InetSocketAddress address, String value, String signature, String cloudService) {
        onlinePlayers.add(new CloudPlayer(username, uniquedId, address, value, signature, TeriumCloud.getTerium().getServiceProvider().getCloudServiceByName(cloudService)));
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutCloudPlayerAdd(username, uniquedId, address, value, signature, cloudService));
    }

    @Override
    public Optional<ICloudPlayer> getCloudPlayer(String username) {
        return onlinePlayers.stream().filter(player -> player.getUsername().equals(username)).toList().stream().findAny();
    }

    @Override
    public Optional<ICloudPlayer> getCloudPlayer(UUID uniqueId) {
        return onlinePlayers.stream().filter(player -> player.getUniqueId().equals(uniqueId)).toList().stream().findAny();
    }

    @Override
    public List<ICloudPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }
}