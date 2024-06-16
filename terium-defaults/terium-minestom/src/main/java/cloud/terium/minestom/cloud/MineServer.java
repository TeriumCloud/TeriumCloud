package cloud.terium.minestom.cloud;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.teriumapi.TeriumAPI;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerCommandEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.PlacementRules;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;

public class MineServer extends TeriumExtension {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        OptifineSupport.enable();
        PlacementRules.init();

        /*
            This is important to paste this in every Minestom Server project
         */
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addEventCallback(AsyncPlayerPreLoginEvent.class, event -> {
            if (TeriumAPI.getTeriumAPI().getProvider().getThisService().isLocked() && !event.getPlayer().hasPermission("terium.locked.join"))
                event.getPlayer().kick(Component.text("§cThis service is locked."));
            TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUuid()).ifPresent(cloudPlayer -> {
                cloudPlayer.updateUsername(event.getPlayer().getUsername());
                cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());
                cloudPlayer.update();
            });
        });

        globalEventHandler.addEventCallback(PlayerDisconnectEvent.class, event -> {
            TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size() - 1);
            TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
        });

        minecraftServer.start("", TeriumAPI.getTeriumAPI().getProvider().getThisService().getPort());
    }
}
