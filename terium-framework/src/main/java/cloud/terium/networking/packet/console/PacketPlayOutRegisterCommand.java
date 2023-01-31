package cloud.terium.networking.packet.console;

import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutRegisterCommand(Command command) implements Packet {

    @Override
    public Command command() {
        return new Command(command.getCommand(), command.getDescription(), command.getAliases()) {
            @Override
            public void execute(String[] args) {
                command.execute(args);
            }
        };
    }
}