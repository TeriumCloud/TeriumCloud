package cloud.terium.minestom.extension;

import cloud.terium.extension.TeriumExtension;
import cloud.terium.minestom.extension.proxy.BungeeCord;
import cloud.terium.minestom.extension.proxy.Velocity;
import cloud.terium.minestom.extension.proxy.util.Proxy;
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

    /**
     * Method to implement every needed terium-cloud utils.
     * ! DO NOT EXECUTE IN YOUR CODE 'minecraftServer#start()' ITS IMPLEMENTED IN THE TERIUM-EXTENSION !
     */
    public TeriumMinestomExtension(MinecraftServer minecraftServer, Proxy proxy) {
        System.out.println(0);
        extension = new TeriumExtension() {
            @Override
            public void executeCommand(String command) {
                MinecraftServer.getSchedulerManager().buildTask(() -> MinecraftServer.getCommandManager().getDispatcher().execute(MinecraftServer.getCommandManager().getConsoleSender(), command));
            }
        };
        System.out.println(1);
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            System.out.println(2);
            extension.successfulStart();

            System.out.println(3);
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

            System.out.println(4);

            globalEventHandler.addEventCallback(PlayerDisconnectEvent.class, event -> {
                extension.getProvider().getThisService().setOnlinePlayers(MinecraftServer.getConnectionManager().getOnlinePlayers().size() - 1);
                extension.getProvider().getThisService().update();
            });

            System.out.println(5);

            if(proxy instanceof Velocity velocity)
                VelocityProxy.enable(velocity.getForwardingSecret());
            else if(proxy instanceof BungeeCord)
                BungeeCordProxy.enable();
            else System.out.println("No vaild Proxy-Instance found!");

            System.out.println(6);

            minecraftServer.start(extension.getProvider().getThisNode().getAddress().getAddress().getHostAddress(), extension.getProvider().getThisService().getPort());
        }).delay(2, TimeUnit.SECOND).schedule();
    }
}
