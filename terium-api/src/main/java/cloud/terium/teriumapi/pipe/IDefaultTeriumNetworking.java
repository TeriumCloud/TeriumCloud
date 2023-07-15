package cloud.terium.teriumapi.pipe;

import io.netty.channel.Channel;

public interface IDefaultTeriumNetworking {

    String getHostAddress();

    int getPort();

    Channel getChannel();

    void addHandler(Handler handler);

    void sendPacket(Packet packet);
}