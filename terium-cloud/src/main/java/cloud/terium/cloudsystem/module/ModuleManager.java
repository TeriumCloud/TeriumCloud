package cloud.terium.cloudsystem.module;

import cloud.terium.cloudsystem.utils.logger.LogType;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.IModuleManager;
import cloud.terium.teriumapi.module.ModuleType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ModuleManager implements IModuleManager {

    private final HashMap<String, IModule> modules;

    @SneakyThrows
    public ModuleManager() {
        this.modules = new HashMap<>();
        final File file = new File("modules//");
        if(file.exists()) {
            if(Arrays.stream(file.listFiles()).toList().isEmpty()) {
                FileUtils.forceDelete(file);
                Logger.log("Modules directory is empty. Terium deleted the emtpy folder.", LogType.WARINING);
                return;
            }

            Logger.log("Trying to load all modules in modules directory...", LogType.INFO);
            for (File modules : file.listFiles()) {
                loadModule(modules.getPath());
            }
            Logger.log("Successfully load all modules in modules directory.", LogType.INFO);
        } else {
            Logger.log("Modules directory is empty or not exist. Terium can't load any modules.", LogType.WARINING);
        }
    }

    @Override
    public void loadModule(String path) {
        try (JarInputStream in = new JarInputStream(
                new BufferedInputStream(Files.newInputStream(new File(path).toPath())))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                if (entry.getName().equals("terium-info.json")) {
                    try (Reader pluginInfoReader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        JsonObject jsonObject = JsonParser.parseReader(pluginInfoReader).getAsJsonObject();

                        if(modules.get(jsonObject.get("name").getAsString()) == null) {
                            modules.put(jsonObject.get("name").getAsString(), new Module(jsonObject.get("name").getAsString(), jsonObject.get("author").getAsString(), jsonObject.get("version").getAsString(), new File(path), ModuleType.valueOf(jsonObject.get("services").getAsString())));
                            Logger.log("Loaded module '" + jsonObject.get("name").getAsString() + "' by '" + jsonObject.get("author").getAsString() + "' v" + jsonObject.get("version").getAsString() + ".", LogType.INFO);
                        }
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    @Override
    public IModule getModuleByName(String name) {
        return modules.get(name);
    }

    @Override
    public List<IModule> getAllModules() {
        return modules.values().stream().toList();
    }
}