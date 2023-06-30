package cloud.terium.plugin.impl.module;

import cloud.terium.networking.packet.module.PacketPlayOutLoadModule;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.module.ILoadedModule;
import cloud.terium.teriumapi.module.IModuleProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ModuleProvider implements IModuleProvider {

    private final List<ILoadedModule> cachedLoadedModules;

    public ModuleProvider() {
        this.cachedLoadedModules = new LinkedList<>();
    }

    @Override
    public void loadModule(String path) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutLoadModule(path));
    }

    @Override
    public Optional<ILoadedModule> getModuleByName(String name) {
        return getAllModules().stream().filter(module -> module.getName().equals(name)).findAny();
    }

    @Override
    public List<ILoadedModule> getAllModules() {
        return cachedLoadedModules;
    }
}
