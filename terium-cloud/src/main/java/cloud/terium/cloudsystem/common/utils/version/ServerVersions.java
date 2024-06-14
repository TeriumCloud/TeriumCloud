package cloud.terium.cloudsystem.common.utils.version;


import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
public enum ServerVersions {
    VELOCITY_3_3_0_SNAPSHOT("velocity-3.3.0-SNAPSHOT", "https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/"),
    VELOCITY_3_2_0_SNAPSHOT("velocity-3.2.0-SNAPSHOT", "https://api.papermc.io/v2/projects/velocity/versions/3.2.0-SNAPSHOT/"),
    BUNGEECORD_LATEST("bungeecord-latest", "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar"),

    SPIGOT_1_16_5("spigot-1.16.5", "https://download.getbukkit.org/spigot/spigot-1.16.5.jar"),
    SPIGOT_1_17_1("spigot-1.17.1", "https://download.getbukkit.org/spigot/spigot-1.17.1.jar"),
    SPIGOT_1_18_1("spigot-1.18.1", "https://download.getbukkit.org/spigot/spigot-1.18.1.jar"),
    SPIGOT_1_18_2("spigot-1.18.2", "https://download.getbukkit.org/spigot/spigot-1.18.2.jar"),
    SPIGOT_1_19_1("spigot-1.19.1", "https://download.getbukkit.org/spigot/spigot-1.19.1.jar"),
    SPIGOT_1_19_2("spigot-1.19.2", "https://download.getbukkit.org/spigot/spigot-1.19.2.jar"),
    SPIGOT_1_20_1("spigot-1.20.1", "https://download.getbukkit.org/spigot/spigot-1.20.1.jar"),
    SPIGOT_1_20_2("spigot-1.20.2", "https://download.getbukkit.org/spigot/spigot-1.20.2.jar"),
    SPIGOT_1_20_3("spigot-1.20.3", "https://download.getbukkit.org/spigot/spigot-1.20.3.jar"),
    SPIGOT_1_20_4("spigot-1.20.4", "https://download.getbukkit.org/spigot/spigot-1.20.4.jar"),
    SPIGOT_1_20_5("spigot-1.20.5", "https://download.getbukkit.org/spigot/spigot-1.20.5.jar"),
    SPIGOT_1_20_6("spigot-1.20.6", "https://download.getbukkit.org/spigot/spigot-1.20.6.jar"),

    PAPER_1_20_6("paper-1.20.6", "https://api.papermc.io/v2/projects/paper/versions/1.20.6/"),
    PAPER_1_20_5("paper-1.20.5", "https://api.papermc.io/v2/projects/paper/versions/1.20.5/"),
    PAPER_1_20_4("paper-1.20.4", "https://api.papermc.io/v2/projects/paper/versions/1.20.4/"),
    PAPER_1_20_3("paper-1.20.3", "https://api.papermc.io/v2/projects/paper/versions/1.20.3/"),
    PAPER_1_20_2("paper-1.20.2", "https://api.papermc.io/v2/projects/paper/versions/1.20.2/"),
    PAPER_1_20_1("paper-1.20.1", "https://api.papermc.io/v2/projects/paper/versions/1.20.1/"),
    PAPER_1_19_4("paper-1.19.4", "https://api.papermc.io/v2/projects/paper/versions/1.19.4/"),
    PAPER_1_19_3("paper-1.19.3", "https://api.papermc.io/v2/projects/paper/versions/1.19.3/"),
    PAPER_1_19_2("paper-1.19.2", "https://api.papermc.io/v2/projects/paper/versions/1.19.2/"),
    PAPER_1_18_2("paper-1.18.2", "https://api.papermc.io/v2/projects/paper/versions/1.18.2/"),
    PAPER_1_17_1("paper-1.17.1", "https://api.papermc.io/v2/projects/paper/versions/1.17.1/"),
    PAPER_1_16_5("paper-1.16.5", "https://api.papermc.io/v2/projects/paper/versions/1.16.5/"),

    PURPUR_1_16_5("purpur-1.16.6", "https://api.purpurmc.org/v2/purpur/1.16.5/"),
    PURPUR_1_17_1("purpur-1.17.1", "https://api.purpurmc.org/v2/purpur/1.17.1/"),
    PURPUR_1_18_2("purpur-1.18.2", "https://api.purpurmc.org/v2/purpur/1.18.2/"),
    PURPUR_1_19_2("purpur-1.19.2", "https://api.purpurmc.org/v2/purpur/1.19.2/"),
    PURPUR_1_19_3("purpur-1.19.3", "https://api.purpurmc.org/v2/purpur/1.19.3/"),
    PURPUR_1_19_4("purpur-1.19.4", "https://api.purpurmc.org/v2/purpur/1.19.4/"),
    PURPUR_1_20_1("purpur-1.20.1", "https://api.purpurmc.org/v2/purpur/1.20.1/"),
    PURPUR_1_20_2("purpur-1.20.2", "https://api.purpurmc.org/v2/purpur/1.20.2/"),
    PURPUR_1_20_3("purpur-1.20.3", "https://api.purpurmc.org/v2/purpur/1.20.3/"),
    PURPUR_1_20_4("purpur-1.20.4", "https://api.purpurmc.org/v2/purpur/1.20.4/"),
    PURPUR_1_20_5("purpur-1.20.5", "https://api.purpurmc.org/v2/purpur/1.20.5/"),
    PURPUR_1_20_6("purpur-1.20.6", "https://api.purpurmc.org/v2/purpur/1.20.6/");


    private final String name;
    private final String url;

    ServerVersions(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @SneakyThrows
    public static String getLatestVersion(ServerVersions serverVersion) {
        if(!serverVersion.name.contains("bungeecord") && !serverVersion.name.contains("spigot")) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(serverVersion.getUrl()).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            String line = "";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            if (serverVersion.getName().startsWith("purpur-")) {
                JsonArray jsonArray = new JsonParser().parse(response.toString()).getAsJsonObject().get("builds").getAsJsonObject().get("all").getAsJsonArray();
                int latestBuild = jsonArray.get(jsonArray.size() - 1).getAsInt();
                return serverVersion.getUrl() + latestBuild + "/download";
            } else {
                JsonArray jsonArray = new JsonParser().parse(response.toString()).getAsJsonObject().get("builds").getAsJsonArray();
                int latestBuild = jsonArray.get(jsonArray.size() - 1).getAsInt();
                return serverVersion.getUrl() + "builds/" + latestBuild + "/downloads/" + serverVersion.getName() + "-" + latestBuild + ".jar";
            }
        }

        return serverVersion.url;
    }
}