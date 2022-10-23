package cloud.terium.bridge.impl.template;

import cloud.terium.networking.packets.PacketPlayOutTemplateCreate;
import cloud.terium.networking.packets.PacketPlayOutTemplateDelete;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.template.ITemplateFactory;

public class TemplateFactory implements ITemplateFactory {

    @Override
    public void createTemplate(String s) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateCreate(s, TeriumAPI.getTeriumAPI().getProvider().getThisService().getServiceGroup().getServiceGroupNode()));
    }

    @Override
    public void createTemplate(String s, INode iNode) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateCreate(s, iNode.getName()));
    }

    @Override
    public void deleteTemplate(String s) {
        TeriumAPI.getTeriumAPI().getProvider().getTeriumNetworking().sendPacket(new PacketPlayOutTemplateDelete(s));
    }
}
