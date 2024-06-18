package cloud.terium.plugin.velocity.listener.cloud;

import cloud.terium.networking.packet.service.PacketPlayOutSuccessfullyServiceStarted;
import cloud.terium.plugin.velocity.TeriumVelocityStartup;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.pipe.Handler;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class VelocityMinestomHandler implements Handler {

    @Override
    public void onReceive(Object object) {
        if(object instanceof PacketPlayOutSuccessfullyServiceStarted serviceStarted && (serviceStarted.parsedCloudService().isPresent() && serviceStarted.parsedCloudService().get().getServiceGroup().getVersion().contains("minestom"))) {
            serviceStarted.parsedCloudService().ifPresent(cloudService -> {
                cloudService.addProperty("proxyType", TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().getVersion());
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("forwarding.secret"), StandardCharsets.UTF_8))) {
                    String secret;
                    while ((secret = bufferedReader.readLine()) != null) {
                        cloudService.addProperty("forwarding-secret", secret);
                    }
                } catch (IOException ignored) {}
                cloudService.update();
            });
        }
    }
}
