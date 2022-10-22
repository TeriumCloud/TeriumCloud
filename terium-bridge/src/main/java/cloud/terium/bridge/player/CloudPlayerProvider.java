package cloud.terium.bridge.player;

import cloud.terium.bridge.TeriumBridge;
import cloud.terium.teriumapi.player.ICloudPlayer;
import cloud.terium.teriumapi.player.ICloudPlayerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.proxy.Player;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Setter
public class CloudPlayerProvider implements ICloudPlayerProvider {

    private final List<ICloudPlayer> registeredPlayers;
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    public CloudPlayerProvider() {
        this.registeredPlayers = new ArrayList<>();

        Arrays.stream(new File("../../data/players").listFiles()).forEach(file -> {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(new File("../../data/players/" + file.getName()).toPath()), StandardCharsets.UTF_8)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                registeredPlayers.add(new CloudPlayer(
                        json.get("username").getAsString(),
                        UUID.fromString(json.get("uniqueId").getAsString()),
                        json.get("first_login").getAsLong(),
                        json.get("last_login").getAsLong(),
                        json.get("address").getAsString()
                ));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void createPlayerJson(Player player) {
        File file = new File("../../data/players/" + player.getUniqueId() + ".json");

        JsonObject json = new JsonObject();
        json.addProperty("username", player.getUsername());
        json.addProperty("uniqueId", player.getUniqueId().toString());
        json.addProperty("first_login", System.currentTimeMillis());
        json.addProperty("last_login", 0);
        json.addProperty("address", player.getRemoteAddress().getAddress().getHostAddress());
        json.add("permissions", new JsonObject());

        executorService.execute(() -> {
            try (final OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                gson.toJson(json, writer);
            } catch (IOException ignored) {}
        });
        registeredPlayers.add(new CloudPlayer(player.getUsername(), player.getUniqueId(), System.currentTimeMillis(), 0, player.getRemoteAddress().getAddress().getHostAddress()));
    }

    public void updateCloudPlayer(ICloudPlayer iCloudPlayer) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(new File("../../data/players/" + iCloudPlayer.getUniqueId() + ".json").toPath()), StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            json.addProperty("username", iCloudPlayer.getUsername());
            json.addProperty("uniqueId", iCloudPlayer.getUniqueId().toString());
            json.addProperty("last_login", System.currentTimeMillis());
            json.addProperty("address", iCloudPlayer.getAddress());

            executorService.execute(() -> {
                try (final OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(new File("../../data/players/" + iCloudPlayer.getUniqueId() + ".json").toPath()), StandardCharsets.UTF_8)) {
                    gson.toJson(json, writer);
                } catch (IOException ignored) {}
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ICloudPlayer getCloudPlayer(String username) {
        return registeredPlayers.stream().filter(iCloudPlayer -> iCloudPlayer.getUsername().equals(username)).findAny().get();
    }

    @Override
    public ICloudPlayer getCloudPlayer(UUID uniqueId) {
        try {
            return registeredPlayers.stream().filter(iCloudPlayer -> iCloudPlayer.getUniqueId().equals(uniqueId)).findAny().get();
        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ICloudPlayer> getRegisteredPlayers() {
        return registeredPlayers;
    }

    @Override
    public List<ICloudPlayer> getOnlinePlayers() {
        return TeriumBridge.getInstance().getPlayerList();
    }
}