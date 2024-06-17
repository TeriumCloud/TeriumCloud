package cloud.terium.minestom.cloud;

import cloud.terium.extension.TeriumExtension;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.extras.PlacementRules;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.extras.velocity.VelocityProxy;

public class MineServer extends TeriumExtension {

    private static TeriumExtension extension;

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        OptifineSupport.enable();
        PlacementRules.init();

        extension = new TeriumExtension() {
            @Override
            public void executeCommand(String command) {
                MinecraftServer.getSchedulerManager().buildTask(() -> MinecraftServer.getCommandManager().getDispatcher().execute(MinecraftServer.getCommandManager().getConsoleSender(), command));
            }
        };

        /*
            This code snipe is important to paste this in every Minestom Server project

            START
         */
        if(extension.getProvider().getThisService().getServiceGroup().getVersion().contains("bungeecord")) {
            BungeeCordProxy.enable();
        }

        if(extension.getProvider().getThisService().getServiceGroup().getVersion().contains("velocity")) {
            VelocityProxy.enable("PASTE YOUR VELOCITY SECRET KEY HERE");
        }

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addEventCallback(AsyncPlayerPreLoginEvent.class, event -> {
            if (extension.getProvider().getThisService().isLocked() && !event.getPlayer().hasPermission("terium.locked.join"))
                event.getPlayer().kick(Component.text("§cThis service is locked."));
            extension.getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            extension.getProvider().getThisService().update();
            extension.getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUuid()).ifPresent(cloudPlayer -> {
                cloudPlayer.updateUsername(event.getPlayer().getUsername());
                cloudPlayer.updateConnectedService(extension.getProvider().getThisService());
                cloudPlayer.update();
            });
        });

        globalEventHandler.addEventCallback(PlayerDisconnectEvent.class, event -> {
            extension.getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size() - 1);
            extension.getProvider().getThisService().update();
        });

        minecraftServer.start(extension.getProvider().getThisNode().getAddress().getAddress().getHostAddress(), extension.getProvider().getThisService().getPort());
        /*
            END
         */

        // your code
    }
}
