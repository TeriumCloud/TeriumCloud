package cloud.terium.cloudsystem.common.event.events.console;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.networking.packet.console.PacketPlayOutSendConsole;
import cloud.terium.teriumapi.TeriumAPI;
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
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendConsole(message, logType));
    }
}