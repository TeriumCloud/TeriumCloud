package cloud.terium.minestom.listener;

import cloud.terium.minestom.MinestomExtension;
import cloud.terium.teriumapi.TeriumAPI;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerCommandEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.permission.Permission;

public class MinestomListener {

    public void register(EventNode<Event> eventEventNode) {
        handleLogin(eventEventNode);
        handleQuit(eventEventNode);
    }


    private void handleLogin(EventNode<Event> eventEventNode) {
        eventEventNode.addListener(AsyncPlayerPreLoginEvent.class, asyncPlayerPreLoginEvent -> {
            if (TeriumAPI.getTeriumAPI().getProvider().getThisService().isLocked() && !asyncPlayerPreLoginEvent.getPlayer().hasPermission("terium.locked.join")) {
                asyncPlayerPreLoginEvent.getPlayer().kick("§cThis service is locked.");
            }

            TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            TeriumAPI.getTeriumAPI().getProvider().getThisService().update();

            TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(asyncPlayerPreLoginEvent.getPlayer().getUuid()).ifPresent(cloudPlayer -> {
                cloudPlayer.updateUsername(asyncPlayerPreLoginEvent.getUsername());
                cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());

                cloudPlayer.update();
            });
        });
    }


    private void handleQuit(EventNode<Event> eventEventNode) {
        eventEventNode.addListener(PlayerDisconnectEvent.class, playerDisconnectEvent -> {
            TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size() - 1);
            TeriumAPI.getTeriumAPI().getProvider().getThisService().update();
        });
    }

    private void commandPreprocess(EventNode<Event> eventEventNode) {
        eventEventNode.addListener(PlayerCommandEvent.class, playerCommandEvent -> {
            if (playerCommandEvent.getCommand().equalsIgnoreCase("rl") ||
                    playerCommandEvent.getCommand().equalsIgnoreCase("reload") ||
                    playerCommandEvent.getCommand().equalsIgnoreCase("rl confirm") ||
                    playerCommandEvent.getCommand().equalsIgnoreCase("reload confirm")) {
                if (playerCommandEvent.getPlayer().hasPermission(new Permission("bukkit.command.reload"))) {
                    playerCommandEvent.setCancelled(true);
                    playerCommandEvent.getPlayer().sendMessage("§cterium-cloud services can't be reloaded.");

                }
            }
        });
    }
}
