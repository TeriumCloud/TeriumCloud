package cloud.terium.minestom;

import cloud.terium.minestom.listener.MinestomListener;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extensions.Extension;

@Getter
public class MinestomExtension extends Extension {

    @Getter
    private static MinestomExtension instance;
    private MinestomListener minestomListener;

    @Override
    public void initialize() {
        instance = this;
        System.out.println("Startup of minestom terium-extension...");
        minestomListener = new MinestomListener();
        minestomListener.register(MinecraftServer.getGlobalEventHandler());
        System.out.println("Startup of bukkit terium-extension succeed...");

    }

    @Override
    public void terminate() {
    }
}
