package cloud.terium.networking.packet.module;

import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.pipe.Packet;

public record PacketPlayOutAddLoadedModule(String name, String fileName, String author, String version, String description, String mainClass, boolean reloadable, ModuleType moduleType) implements Packet {
}
