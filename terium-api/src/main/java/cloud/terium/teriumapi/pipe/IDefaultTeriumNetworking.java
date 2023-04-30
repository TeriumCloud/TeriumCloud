package cloud.terium.teriumapi.pipe;

import io.netty.channel.Channel;

public interface IDefaultTeriumNetworking {

    Channel getChannel();

    void addHandler(Handler handler);

    void sendPacket(Packet packet);
}