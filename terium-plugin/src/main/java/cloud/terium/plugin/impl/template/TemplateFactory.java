package cloud.terium.plugin.impl.template;

import cloud.terium.networking.packet.PacketPlayOutTemplateCreate;
import cloud.terium.networking.packet.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateFactory;

public class TemplateFactory implements ITemplateFactory {

    @Override
    public ITemplate createTemplate(String name) {
        ITemplate template = new PacketPlayOutTemplateCreate(name);
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateCreate(name));
        return template;
    }

    @Override
    public void deleteTemplate(String name) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateDelete(TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(name).orElseGet(null)));
    }
}
