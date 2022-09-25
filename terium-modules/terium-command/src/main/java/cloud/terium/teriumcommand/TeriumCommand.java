package cloud.terium.teriumcommand;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumcommand.utils.CommandUtils;
import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
        id = "terium-cloudcommand",
        name = "TeriumCommand",
        version = "1.0-DEVELOPMENT",
        description = "/cloud command for terium-cloud",
        authors = {"Jxnnik"}
)
public class TeriumCommand {

    private final ProxyServer proxyServer;

    @Inject
    public TeriumCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getCommandManager().register(buildCloudCommand());
        // proxyServer.getCommandManager().register(buildTeriumCommand());
    }

    private BrigadierCommand buildCloudCommand() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("cloud")
                .requires(commandSource -> commandSource.hasPermission("terium.command"))
                .then(LiteralArgumentBuilder.<CommandSource>literal("help")
                        .executes(CommandUtils::sendHelpMessage))
                .build();

        return new BrigadierCommand(node);
    }
}