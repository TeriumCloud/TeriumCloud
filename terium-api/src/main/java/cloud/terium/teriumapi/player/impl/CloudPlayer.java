package cloud.terium.teriumapi.player.impl;

import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class CloudPlayer implements ICloudPlayer {

    private String username;
    private UUID uniqueId;
    private String address;
    private Optional<ICloudService> connectedService;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Optional<ICloudService> getConnectedCloudService() {
        return connectedService;
    }

    @Override
    public void updateUsername(String username) {
        this.username = username;
    }

    @Override
    public void updateUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public void updateAddress(String address) {
        this.address = address;
    }

    @Override
    public void updateConnectedService(ICloudService cloudService) {
        this.connectedService = Optional.of(cloudService);
    }
}
