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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ModuleProvider implements IModuleProvider {

    private final HashMap<String, ILoadedModule> loadedModuleCache;

    public ModuleProvider() {
        this.loadedModuleCache = new HashMap<>();
    }

    public void loadModules() {
        File file = new File("modules//");
        if (!file.exists()) file.mkdirs();
        Arrays.stream(file.listFiles()).toList().forEach(module -> loadModule(module.getPath()));
    }

    @SneakyThrows
    public void executeModule(File file, String mainClass, String methode) {
        try {
            Class<?> cl = new URLClassLoader(new URL[]{file.toURL()}, Thread.currentThread().getContextClassLoader()).loadClass(mainClass);
            Class<?> moduleClass = Class.forName(mainClass, true, cl.getClassLoader());

            IModule cloudModule = (IModule) moduleClass.newInstance();

            if (methode.equals("enable")) cloudModule.onEnable();
            else cloudModule.onDisable();
        } catch (MalformedURLException | ClassNotFoundException exception) {
            exception.printStackTrace();
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

                        if (loadedModuleCache.get(jsonObject.get("name").getAsString()) == null) {
                            loadedModuleCache.put(jsonObject.get("name").getAsString(), new ILoadedModule() {
                                @Override
                                public String getName() {
                                    return jsonObject.get("name").getAsString();
                                }

                                @Override
                                public String getFileName() {
                                    return new File(path).getName();
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
                                    return jsonObject.get("main-class").getAsString();
                                }

                                @Override
                                public ModuleType getModuleType() {
                                    return ModuleType.valueOf(jsonObject.get("type").getAsString());
                                }
                            });

                            Logger.log("Loaded module '" + jsonObject.get("name").getAsString() + "' by '" + jsonObject.get("author").getAsString() + "' v" + jsonObject.get("version").getAsString() + ".", LogType.INFO);
                            executeModule(new File(path), jsonObject.get("main-class").getAsString(), "enable");
                            in.close();
                            return;
                        }
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void unloadModule(ILoadedModule module) {
        loadedModuleCache.remove(module.getName());
        executeModule(new File("modules//" + module.getFileName()), module.getMainClass(), "disable");
    }

    @Override
    public Optional<ILoadedModule> getModuleByName(String name) {
        return Optional.ofNullable(loadedModuleCache.get(name));
    }

    @Override
    public List<ILoadedModule> getAllModules() {
        return loadedModuleCache.values().stream().toList();
    }
}