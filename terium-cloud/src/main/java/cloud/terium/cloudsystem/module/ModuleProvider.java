package cloud.terium.cloudsystem.module;

import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.module.ILoadedModule;
import cloud.terium.teriumapi.module.IModule;
import cloud.terium.teriumapi.module.IModuleProvider;
import cloud.terium.teriumapi.module.ModuleType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ModuleProvider implements IModuleProvider {

    private final HashMap<String, ILoadedModule> loadedModuleCache;

    public ModuleProvider() {
        this.loadedModuleCache = new HashMap<>();
    }

    @SneakyThrows
    public IModule executeModule(String mainClass, String methode) {
        Class<?> description = Class.forName(mainClass);
        IModule cloudModule = (IModule) description.newInstance();

        if(methode.equals("enable")) cloudModule.onEnable(); else cloudModule.onDisable();

        return cloudModule;
    }

    @Override
    public void loadModule(String path) {
        try (JarInputStream in = new JarInputStream(
                new BufferedInputStream(Files.newInputStream(new File(path).toPath())))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                if (entry.getName().equals("terium-module.json")) {
                    try (Reader pluginInfoReader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        JsonObject jsonObject = JsonParser.parseReader(pluginInfoReader).getAsJsonObject();

                        if (loadedModuleCache.get(jsonObject.get("name").getAsString()) == null) {
                            loadedModuleCache.put(jsonObject.get("name").getAsString(), new ILoadedModule() {
                                @Override
                                public String getName() {
                                    return jsonObject.get("name").getAsString();
                                }

                                @Override
                                public String getAuthor() {
                                    return jsonObject.get("author").getAsString();
                                }

                                @Override
                                public String getVersion() {
                                    return jsonObject.get("version").getAsString();
                                }

                                @Override
                                public String getDescription() {
                                    return jsonObject.get("description").getAsString();
                                }

                                @Override
                                public String getMainClass() {
                                    return jsonObject.get("mainclass").getAsString();
                                }

                                @Override
                                public ModuleType getModuleType() {
                                    return ModuleType.valueOf(jsonObject.get("type").getAsString());
                                }
                            });
                            Logger.log("Loaded module '" + jsonObject.get("name").getAsString() + "' by '" + jsonObject.get("author").getAsString() + "' v" + jsonObject.get("version").getAsString() + ".", LogType.INFO);
                        }
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public ILoadedModule getModuleByName(String name) {
        return loadedModuleCache.get(name);
    }

    @Override
    public List<ILoadedModule> getAllModules() {
        return loadedModuleCache.values().stream().toList();
    }
}