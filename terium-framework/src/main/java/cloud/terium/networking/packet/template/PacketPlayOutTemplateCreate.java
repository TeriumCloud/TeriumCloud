package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;

public record PacketPlayOutTemplateCreate(String name) implements Packet, ITemplate {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Path getPath() {
        return Path.of("templates\\" + name);
    }
}