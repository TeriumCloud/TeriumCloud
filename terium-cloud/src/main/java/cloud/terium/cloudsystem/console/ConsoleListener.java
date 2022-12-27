package cloud.terium.cloudsystem.console;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.event.events.console.RegisterCommandEvent;
import cloud.terium.cloudsystem.event.events.console.SendConsoleEvent;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

public class ConsoleListener implements Listener {

    @Subscribe
    public void handleRegisterCommand(RegisterCommandEvent event) {
        TeriumCloud.getTerium().getCommandManager().registerCommand(event.getCommand());
    }

    @Subscribe
    public void handleSendConsole(SendConsoleEvent event) {
        Logger.log(event.getMessage(), event.getLogType());
    }
}
