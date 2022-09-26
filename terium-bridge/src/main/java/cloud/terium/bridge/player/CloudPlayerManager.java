package cloud.terium.bridge.player;

import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.Player;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudPlayerManager implements ICloudPlayerManager {

    /*
     * TODO: Recode that class
     */
    public void createPlayerJson(Player player) {
        File file = new File("../../data/players/" + player.getUsername() + ".json");
        file.mkdirs();

        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        json.addProperty("username", player.getUsername());
        json.addProperty("uniqueId", player.getUniqueId().toString());
        json.addProperty("first_login", System.currentTimeMillis());
        json.addProperty("last_login", 0);
        json.addProperty("address", player.getRemoteAddress().getAddress().getHostName());
        json.add("permissions", new JsonObject());

        executorService.execute(() -> {
            try (final OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                gson.toJson(json, writer);
            } catch (IOException ignored) {}
        });
    }

    public void updateCloudPlayer(ICloudPlayer iCloudPlayer) {
        // Update everything
    }

    public void updateCloudPlayer(ICloudPlayer iCloudPlayer, String property, Object value) {
        // Update only one value
    }

    @Override
    public ICloudPlayer getCloudPlayer(String username) {
        return null;
    }

    @Override
    public ICloudPlayer getCloudPlayer(UUID uniqueId) {
        return null;
    }

    @Override
    public List<ICloudPlayer> getRegisteredPlayers() {
        return null;
    }

    @Override
    public List<ICloudPlayer> getOnlinePlayers() {
        return null;
    }
}