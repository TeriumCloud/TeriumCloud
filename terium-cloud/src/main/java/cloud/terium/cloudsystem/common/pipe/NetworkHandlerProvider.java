package cloud.terium.cloudsystem.common.pipe;

import cloud.terium.cloudsystem.common.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.pipe.Handler;
import cloud.terium.teriumapi.pipe.Packet;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NetworkHandlerProvider {

    private final List<Handler> handlers;

    public NetworkHandlerProvider() {
        this.handlers = new ArrayList<>();
        Logger.log("Loaded network-handler-provider", LogType.INFO);
    }

    public void addHandler(Handler listener) {
        handlers.add(listener);
    }

    @SneakyThrows
    public <T extends Packet> void callSendingPacket(T packet) {
        for (Handler handler : handlers) {
            Method method = handler.getClass().getDeclaredMethod("onReceive", Object.class);

            try {
                method.invoke(handler, packet);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }
}
