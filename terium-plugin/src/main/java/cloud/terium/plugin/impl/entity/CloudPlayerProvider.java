package cloud.terium.plugin.impl.entity;

import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.entity.ICloudPlayerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CloudPlayerProvider implements ICloudPlayerProvider {

    private final List<ICloudPlayer> onlinePlayers = new ArrayList<>();

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