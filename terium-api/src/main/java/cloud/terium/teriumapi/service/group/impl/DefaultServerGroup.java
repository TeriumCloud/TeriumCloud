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

public class DefaultServerGroup {

    public DefaultServerGroup(String name, String groupTitle, String node, boolean maintenance, int maximumPlayers, int memory, int minimalServices, int maximalServices) {
        new File("templates//" + name).mkdirs();

        JsonObject json;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (final PrintWriter writer = new PrintWriter("groups/" + CloudServiceType.Server + "/" + name + ".json")) {
            writer.print(gson.toJson(json = new JsonObject()));
            json.addProperty("group_name", name);
            json.addProperty("group_title", groupTitle);
            json.addProperty("node", node);
            json.addProperty("servicetype", CloudServiceType.Server.name());
            json.addProperty("maintenance", maintenance);
            json.addProperty("maximum_players", maximumPlayers);
            json.addProperty("memory", memory);
            json.addProperty("minimal_services", minimalServices);
            json.addProperty("maximal_services", maximalServices);

            executorService.execute(() -> {
                gson.toJson(json, writer);
            });
        } catch (FileNotFoundException ignored) { }
    }
}