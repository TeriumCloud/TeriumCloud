package cloud.terium.cloudsystem.cluster.entity;

import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.player.CloudPlayerUpdateEvent;

public class CloudPlayerListener implements Listener {

    @Subscribe
    public void handleCloudPlayerUpdate(CloudPlayerUpdateEvent event) {
        event.getCloudPlayer().updateUsername(event.getUsername());
        event.getCloudPlayer().updateAddress(event.getAddress());
        event.getCloudPlayer().updateConnectedService(event.getConnectedService());
        event.getCloudPlayer().updateSkinData(event.getValue(), event.getSignature());
    }
}