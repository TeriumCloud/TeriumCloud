package cloud.terium.networking.packet.module;

import cloud.terium.teriumapi.module.ModuleType;
import cloud.terium.teriumapi.network.Packet;

public record PacketPlayOutAddLoadedModule(String name, String fileName, String author, String version, String description, String mainClass, ModuleType moduleType) implements Packet {
}
