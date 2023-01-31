package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.template.ITemplate;

import java.nio.file.Path;

public record PacketPlayOutTemplateDelete(ITemplate template) implements Packet {

    @Override
    public ITemplate template() {
        return new ITemplate() {
            @Override
            public String getName() {
                return template.getName();
            }

            @Override
            public Path getPath() {
                return template.getPath();
            }
        };
    }
}