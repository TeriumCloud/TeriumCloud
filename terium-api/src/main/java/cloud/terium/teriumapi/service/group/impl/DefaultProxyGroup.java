package cloud.terium.teriumapi.service.group.impl;

import cloud.terium.teriumapi.service.CloudServiceType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultProxyGroup {

    public DefaultProxyGroup(String name, String groupTitle, String node, boolean maintenance, int port, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new File("templates//" + name).mkdirs();

        final JsonObject json;
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (final PrintWriter writer = new PrintWriter("groups/" + CloudServiceType.Proxy + "/" + name + ".json")) {
            writer.print(gson.toJson(json = new JsonObject()));
            json.addProperty("group_name", name);
            json.addProperty("group_title", groupTitle);
            json.addProperty("node", node);
            json.addProperty("servicetype", CloudServiceType.Proxy.name());
            json.addProperty("port", port);
            json.addProperty("maintenance", maintenance);
            json.addProperty("maximum_players", maximumPlayers);
            json.addProperty("memory", memory);
            json.addProperty("minimal_services", minimalServices);
            json.addProperty("maximal_services", maximalServices);

            executorService.execute(() -> {
                gson.toJson(json, writer);
            });
        } catch (final FileNotFoundException ignored) { }
    }
}