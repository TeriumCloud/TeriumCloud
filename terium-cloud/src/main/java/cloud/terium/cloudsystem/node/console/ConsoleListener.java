package cloud.terium.cloudsystem.node.console;

import cloud.terium.cloudsystem.common.event.events.console.RegisterCommandEvent;
import cloud.terium.cloudsystem.common.event.events.console.SendConsoleEvent;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class ConsoleListener implements Listener {

    @Subscribe
    public void handleRegisterCommand(RegisterCommandEvent event) {
        NodeStartup.getNode().getCommandManager().registerCommand(event.getCommand());
    }

    @Subscribe
    public void handleSendConsole(SendConsoleEvent event) {
        Logger.log(event.getMessage(), event.getLogType());
    }
}
