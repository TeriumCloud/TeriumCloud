package cloud.terium.minestom;


import net.minestom.server.Bootstrap;
import net.minestom.server.MinecraftServer;

public class MinestomStartup {

    public static void main(String[] args) {
        Bootstrap.bootstrap("cloud.terium.minestom.cloud.MineServer", args);
    }

}
