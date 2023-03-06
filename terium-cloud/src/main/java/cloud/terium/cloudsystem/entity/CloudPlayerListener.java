package cloud.terium.cloudsystem.entity;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.player.CloudPlayerJoinEvent;
import cloud.terium.cloudsystem.event.events.player.CloudPlayerQuitEvent;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.player.CloudPlayerUpdateEvent;

public class CloudPlayerListener implements Listener {

    @Subscribe
    public void handleCloudPlayerJoin(CloudPlayerJoinEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).ifPresentOrElse(cloudPlayer -> {
            Logger.log("Player " + cloudPlayer.getUsername() + " is joined the network.", LogType.INFO);
        }, () -> {
            Logger.log("Player with uuid " + event.getCloudPlayer() + " is not registered!", LogType.ERROR);
        });
    }

    @Subscribe
    public void handleCloudPlayerJoin(CloudPlayerQuitEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).ifPresentOrElse(cloudPlayer -> {
            Logger.log("Player " + cloudPlayer.getUsername() + " has left the network.", LogType.INFO);
            TeriumCloud.getTerium().getCloudPlayerProvider().unregisterPlayer(TeriumCloud.getTerium().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).orElseGet(null));
        }, () -> {
            Logger.log("Player with uuid " + event.getCloudPlayer() + " is not registered!", LogType.ERROR);
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