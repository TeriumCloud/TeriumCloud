package cloud.terium.cloudsystem.entity;

import cloud.terium.cloudsystem.event.events.player.CloudPlayerJoinEvent;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class CloudPlayerListener implements Listener {

    @Subscribe
    public void handleCloudPlayerJoin(CloudPlayerJoinEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getCloudPlayer()).ifPresentOrElse(cloudPlayer -> {
            Logger.log("Player " + cloudPlayer.getUsername() + " is joined the network.", LogType.INFO);
        }, () -> {
            Logger.log("Player with uuid " + event.getCloudPlayer() + " is not registered!", LogType.ERROR);
        });
    }
}