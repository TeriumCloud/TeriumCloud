package cloud.terium.networking.packet.template;

import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.network.Packet;
import cloud.terium.teriumapi.template.ITemplate;

import java.util.Optional;

public record PacketPlayOutTemplateDelete(String template) implements Packet {

    public Optional<ITemplate> parsedTemplate() {
        return TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(template);
    }
}