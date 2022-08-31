package cloud.terium.teriumapi.player;

import cloud.terium.teriumapi.service.ICloudService;

import java.util.UUID;

public interface ICloudPlayer {

    String getUsername();

    UUID getUniqueId();

    long firstLogin();

    long lastLogin();

    String getAddress();

    ICloudService getConnectedCloudService();

    void connectWithService();
}
