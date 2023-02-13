package cloud.terium.cloudsystem.entity;

import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CloudPlayerProvider implements ICloudPlayerProvider {

    private final List<ICloudPlayer> registeredPlayers = new ArrayList<>();
    private final List<ICloudPlayer> onlinePlayers = new ArrayList<>();

    public void registerPlayer(String username, UUID uniuqedId, InetSocketAddress address) {

    }

    @Override
    public Optional<ICloudPlayer> getCloudPlayer(String username) {
        return registeredPlayers.stream().filter(player -> player.getUsername().equals(username)).toList().stream().findAny();
    }

    @Override
    public Optional<ICloudPlayer> getCloudPlayer(UUID uniqueId) {
        return registeredPlayers.stream().filter(player -> player.getUniqueId().equals(uniqueId)).toList().stream().findAny();
    }

    @Override
    public List<ICloudPlayer> getRegisteredPlayers() {
        return registeredPlayers;
    }

    @Override
    public List<ICloudPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }
}