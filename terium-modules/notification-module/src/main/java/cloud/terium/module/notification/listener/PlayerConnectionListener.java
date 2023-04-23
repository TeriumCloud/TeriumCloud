package cloud.terium.module.notification.listener;

import cloud.terium.module.notification.velocity.NotificationVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.player.CloudPlayerJoinEvent;
import cloud.terium.teriumapi.events.player.CloudPlayerQuitEvent;
import cloud.terium.teriumapi.events.player.CloudPlayerServiceConnectedEvent;

public class PlayerConnectionListener implements Listener {

    /**
     * @param event
     * if the "logPlayerConnections" option is enabled in the config.json configuration file, the cloud console will print a notification when they connect to the network
     */
    @Subscribe
    public void onPlayerConnect(CloudPlayerJoinEvent event) {
        if (NotificationVelocityStartup.getInstance().getConfigManager().getJson().get("logPlayerConnections").getAsBoolean()) {
            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole(String.format("%s (%s) connected to the network", event.getCloudPlayer().getUsername(), event.getCloudPlayer().getUniqueId()), LogType.INFO);
        }
    }

    /**
     * @param event
     * if the "logPlayerConnections" option is enabled in the config.json configuration file, the cloud console will print a notification when they disconect from the network
     */
    @Subscribe
    public void onPlayerDisconnect(CloudPlayerQuitEvent event) {
        if (NotificationVelocityStartup.getInstance().getConfigManager().getJson().get("logPlayerConnections").getAsBoolean()) {
            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole(String.format("%s (%s) diconnected from the network", event.getCloudPlayer().getUsername(), event.getCloudPlayer().getUniqueId()), LogType.INFO);
        }
    }

    /**
     * @param event
     * if the "logPlayerConnections" option is enabled in the config.json configuration file, the cloud console will print a notification when they switch to a new service
     */
    @Subscribe
    public void onPlayerJoin(CloudPlayerServiceConnectedEvent event) {
        if (NotificationVelocityStartup.getInstance().getConfigManager().getJson().get("logPlayerConnections").getAsBoolean()) {
            TeriumAPI.getTeriumAPI().getProvider().getConsoleProvider().sendConsole(String.format("%s (%s) switched to %s", event.getCloudPlayer().getUsername(), event.getCloudPlayer().getUniqueId(), event.getCloudService().getServiceName()), LogType.INFO);
        }
    }



}
