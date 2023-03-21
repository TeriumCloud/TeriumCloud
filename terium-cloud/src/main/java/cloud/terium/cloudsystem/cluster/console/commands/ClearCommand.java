package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.console.command.Command;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Clear the console.");
    }

    @Override
    public void execute(String[] args) {
        ClusterStartup.getCluster().getConsoleManager().clearScreen();

        Logger.log("""
                §f_______ _______  ______ _____ _     _ _______ §b_______         _____  _     _ ______\s
                §f   |    |______ |_____/   |   |     | |  |  | §b|       |      |     | |     | |     \\
                §f   |    |______ |    \\_ __|__ |_____| |  |  | §b|_____  |_____ |_____| |_____| |_____/ §7[§f%version%§7]
                                                                                            \s
                §7> §fTerium by ByRaudy(Jannik H.)\s
                §7> §fDiscord: §bterium.cloud/discord §f| Twitter: §b@teriumcloud§f
                                
                """.replace("%version%", TeriumCloud.getTerium().getCloudUtils().getVersion()));
    }
}
