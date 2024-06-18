package cloud.terium.minestom.cloud;

import cloud.terium.minestom.TeriumMinestomExtension;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.PlacementRules;
import net.minestom.server.extras.optifine.OptifineSupport;

public class MineServer {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        OptifineSupport.enable();
        PlacementRules.init();

        new TeriumMinestomExtension(minecraftServer);
    }
}
