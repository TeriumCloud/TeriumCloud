package cloud.terium.cloudsystem.module;

import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.service.CloudServiceType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class ModuleManager {

    private final HashMap<String, Module> modules;

    @SneakyThrows
    public ModuleManager() {
        this.modules = new HashMap<>();
        Logger.log("Trying to load all modules in modules directory...", LogType.INFO);
        for (File file : new File("modules//").listFiles()) {
            loadModule(file.getPath());
        }
        Logger.log("Successfully load all modules in modules directory.", LogType.INFO);
    }

    public void loadModule(String path) {
        try (JarInputStream in = new JarInputStream(
                new BufferedInputStream(Files.newInputStream(new File(path).toPath())))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                if (entry.getName().equals("terium-info.json")) {
                    try (Reader pluginInfoReader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        JsonObject jsonObject = JsonParser.parseReader(pluginInfoReader).getAsJsonObject();

                        if(modules.get(jsonObject.get("name").getAsString()) == null) {
                            modules.put(jsonObject.get("name").getAsString(), new Module(jsonObject.get("name").getAsString(), jsonObject.get("author").getAsString(), jsonObject.get("version").getAsString(), CloudServiceType.valueOf(jsonObject.get("services").getAsString())));
                            Logger.log("Loaded module '" + jsonObject.get("name").getAsString() + "' by '" + jsonObject.get("author").getAsString() + "' v" + jsonObject.get("version").getAsString() + ".", LogType.INFO);
                        }
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    public Module getModuleByName(String name) {
        return modules.get(name);
    }

    public List<Module> getAllModules() {
        return modules.values().stream().toList();
    }
}