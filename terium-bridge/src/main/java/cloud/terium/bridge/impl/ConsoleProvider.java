package cloud.terium.bridge.impl;

import cloud.terium.networking.packets.PacketPlayOutExecuteConsole;
import cloud.terium.networking.packets.PacketPlayOutSendConsole;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.ICloudConsoleProvider;
import cloud.terium.teriumapi.console.LogType;

public class ConsoleProvider implements ICloudConsoleProvider {

    @Override
    public void sendConsole(String s, LogType logType) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutSendConsole(s, logType));
    }

    @Override
    public void executeCommand(String s) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutExecuteConsole(s));
    }
}
