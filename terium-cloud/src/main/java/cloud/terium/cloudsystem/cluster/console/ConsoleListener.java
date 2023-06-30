package cloud.terium.cloudsystem.cluster.console;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.cloudsystem.common.event.events.console.RegisterCommandEvent;
import cloud.terium.cloudsystem.common.event.events.console.SendConsoleEvent;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

;

public class ConsoleListener implements Listener {

    @Subscribe
    public void handleRegisterCommand(RegisterCommandEvent event) {
        ClusterStartup.getCluster().getCommandManager().registerCommand(event.getCommand());
    }

    @Subscribe
    public void handleSendConsole(SendConsoleEvent event) {
        Logger.log(event.getMessage(), event.getLogType());
    }
}
