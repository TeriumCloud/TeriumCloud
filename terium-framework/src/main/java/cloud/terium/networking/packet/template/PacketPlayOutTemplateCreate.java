package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.pipe.Packet;

import java.nio.file.Path;

public record PacketPlayOutTemplateCreate(String name) implements Packet {

    public Path path() {
        return Path.of("templates\\" + name);
    }
}