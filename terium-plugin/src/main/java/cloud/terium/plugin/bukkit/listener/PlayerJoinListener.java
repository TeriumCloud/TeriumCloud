package cloud.terium.plugin.bukkit.listener;

import cloud.terium.teriumapi.TeriumAPI;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        TeriumAPI.getTeriumAPI().getProvider().getThisService().setOnlinePlayers(TeriumAPI.getTeriumAPI().getProvider().getThisService().getOnlinePlayers() + 1);
        TeriumAPI.getTeriumAPI().getProvider().getThisService().update();

        TeriumAPI.getTeriumAPI().getProvider().getCloudPlayerProvider().getCloudPlayer(event.getPlayer().getUniqueId()).ifPresent(cloudPlayer -> {
            cloudPlayer.updateUsername(event.getPlayer().getName());
            cloudPlayer.updateConnectedService(TeriumAPI.getTeriumAPI().getProvider().getThisService());

            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + event.getPlayer().getUniqueId().toString().replace("-", "") + "?unsigned=false");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == 204) return;
                JsonObject object = (JsonObject) new JsonParser().parse(new InputStreamReader(connection.getInputStream()));
                JsonObject properties = (JsonObject) object.getAsJsonArray("properties").get(0);
                cloudPlayer.updateSkinData(properties.get("value").getAsString(), properties.get("signature").getAsString());
            } catch (Exception ignored) {}

            cloudPlayer.update();
        });
    }
}