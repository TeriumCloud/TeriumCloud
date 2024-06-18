package cloud.terium.plugin.bungeecord.listener.cloud;

import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BungeeMinestomHandler implements Handler {

    @Override
    public void onReceive(Object object) {
        if(object instanceof PacketPlayOutSuccessfullyServiceStarted serviceStarted && (serviceStarted.parsedCloudService().isPresent() && serviceStarted.parsedCloudService().get().getServiceGroup().getVersion().contains("minestom"))) {
            serviceStarted.parsedCloudService().ifPresent(cloudService -> {
                cloudService.addProperty("proxyType", TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().getVersion());
                cloudService.update();
            });
        }
    }
}
