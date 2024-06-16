package cloud.terium.extension.impl.template;

import cloud.terium.networking.packet.template.PacketPlayOutTemplateCreate;
import cloud.terium.networking.packet.template.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateFactory;
import cloud.terium.teriumapi.template.impl.Template;

import java.nio.file.Path;

public class TemplateFactory implements ITemplateFactory {

    @Override
    public ITemplate createTemplate(String name) {
        ITemplate template = new Template(name, Path.of("templates\\" + name));
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateCreate(name));
        return template;
    }

    @Override
    public void deleteTemplate(String name) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateDelete(name));
    }
}
