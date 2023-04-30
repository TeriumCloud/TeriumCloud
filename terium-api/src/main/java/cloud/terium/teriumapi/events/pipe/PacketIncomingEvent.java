package cloud.terium.teriumapi.events.pipe;

import cloud.terium.teriumapi.event.Event;
import io.netty.channel.Channel;
import lombok.Getter;

@Getter
public class PacketIncomingEvent extends Event {

    private final Channel channel;
    private final Object object;

    public PacketIncomingEvent(Channel channel, Object object) {
        this.channel = channel;
        this.object = object;
    }
}
