package cloud.terium.minestom.cloud.impl.console;

import cloud.terium.networking.packet.console.PacketPlayOutSendConsole;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.IConsoleProvider;
import cloud.terium.teriumapi.console.LogType;

public class ConsoleProvider implements IConsoleProvider {

    @Override
    public void sendConsole(String message, LogType logType) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendConsole(message, logType));
    }
}
