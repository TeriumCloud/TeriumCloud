package cloud.terium.teriumapi.entity.impl;

import cloud.terium.teriumapi.entity.ICloudPlayer;
import cloud.terium.teriumapi.service.ICloudService;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class CloudPlayer implements ICloudPlayer {

    private String username;
    private UUID uniqueId;
    private InetSocketAddress address;
    private String skinValue;
    private String skinSignature;
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
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public String getSkinValue() {
        return skinValue;
    }

    @Override
    public String getSkinSignature() {
        return skinSignature;
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
    public void updateAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void updateConnectedService(ICloudService cloudService) {
        this.connectedService = Optional.of(cloudService);
    }

    @Override
    public void updateSkinData(String skinValue, String skinSignature) {
        this.skinValue = skinValue;
        this.skinSignature = skinSignature;
    }
}
