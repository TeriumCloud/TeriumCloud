package cloud.terium.cloudsystem.event.events.console;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.networking.packet.console.PacketPlayOutSendConsole;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.event.Event;
import lombok.Getter;

@Getter
public class SendConsoleEvent extends Event {

    private final String message;
    private final LogType logType;

    public SendConsoleEvent(String message, LogType logType) {
        this.message = message;
        this.logType = logType;
        TeriumCloud.getTerium().getNetworking().sendPacket(new PacketPlayOutSendConsole(message, logType));
    }
}