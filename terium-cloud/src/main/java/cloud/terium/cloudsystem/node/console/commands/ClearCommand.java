package cloud.terium.cloudsystem.node.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.teriumapi.console.command.Command;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Clear the console.");
    }

    @Override
    public void execute(String[] args) {
        NodeStartup.getNode().getConsoleManager().clearScreen();

        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b__   _  _____  ______  _______
                 §f  |    |______ |_____/   |   |     | |  |  | §b| \\  | |     | |     \\ |______
                 §f  |    |______ |    \\_ __|__ |_____| |  |  | §b|  \\_| |_____| |_____/ |______ §7[§f%version%§7]
                                                                                                 \s
                 §7> §fTerium by ByRaudy(Jannik H.)\s
                 §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                 """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()));
    }
}
