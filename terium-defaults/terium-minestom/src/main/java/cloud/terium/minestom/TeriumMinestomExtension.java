package cloud.terium.minestom;

import cloud.terium.extension.TeriumExtension;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.utils.time.TimeUnit;

public class TeriumMinestomExtension {

    private final TeriumExtension extension;

    public TeriumMinestomExtension(MinecraftServer minecraftServer) {
        extension = new TeriumExtension() {
            @Override
            public void executeCommand(String command) {
                MinecraftServer.getSchedulerManager().buildTask(() -> MinecraftServer.getCommandManager().getDispatcher().execute(MinecraftServer.getCommandManager().getConsoleSender(), command));
            }
        };
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            extension.successfulStart();

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

            MinecraftServer.getSchedulerManager().buildTask(() -> {
                if (extension.getProvider().getThisService().getPropertyMap().containsKey("proxyType")) {
                    if (((String) extension.getProvider().getThisService().getProperty("proxyType")).contains("bungeecord")) {
                        BungeeCordProxy.enable();
                        System.out.println("TERIUM: Successfully init BungeeCordProxy.class.");
                    }

                    if (((String) extension.getProvider().getThisService().getProperty("proxyType")).contains("velocity")) {
                        System.out.println("velocity auf die 1");
                        VelocityProxy.enable((String) extension.getProvider().getThisService().getProperty("forwarding-secret"));
                        System.out.println("TERIUM: Successfully init VelocityProxy.class.");
                    }
                }

                minecraftServer.start(extension.getProvider().getThisNode().getAddress().getAddress().getHostAddress(), extension.getProvider().getThisService().getPort());
            }).delay(2, TimeUnit.SECOND).schedule();
        }).delay(2, TimeUnit.SECOND).schedule();
    }
}
