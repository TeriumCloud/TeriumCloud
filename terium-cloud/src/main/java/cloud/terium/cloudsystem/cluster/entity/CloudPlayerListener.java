package cloud.terium.cloudsystem.cluster.entity;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.common.event.events.player.CloudPlayerQuitEvent;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.player.CloudPlayerUpdateEvent;

public class CloudPlayerListener implements Listener {

    @Subscribe
    public void handleCloudPlayerQuit(CloudPlayerQuitEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).ifPresent(cloudPlayer -> {
            ClusterStartup.getCluster().getCloudPlayerProvider().unregisterPlayer(ClusterStartup.getCluster().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).orElseGet(null));
        });
    }

    @Subscribe
    public void handleCloudPlayerUpdate(CloudPlayerUpdateEvent event) {
        event.getCloudPlayer().updateUsername(event.getUsername());
        event.getCloudPlayer().updateAddress(event.getAddress());
        event.getCloudPlayer().updateConnectedService(event.getConnectedService());
        event.getCloudPlayer().updateSkinData(event.getValue(), event.getSignature());
    }
}