package cloud.terium.cloudsystem.event;

import cloud.terium.teriumapi.event.Event;
import cloud.terium.teriumapi.event.IEventProvider;
import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventProvider implements IEventProvider {

    private static final List<Listener> listeners = new ArrayList<>();

    @Override
    public <T extends Event> void callEvent(T event) {
        for (Listener Listener : listeners) {
            if(!Listener.isActive()) {
                continue;
            }
            for (Method method : Listener.getClass().getDeclaredMethods()) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if(subscribe == null) {
                    continue;
                }
                if(method.getParameterCount() != 1) {
                    System.out.println("[TERIUM-EVENTSYSTEM]: @Subscribe method with more or less then 1 arguments can't be invoked: " + method.getName());
                    continue;
                }
                if(!Arrays.asList(method.getParameterTypes()).contains(event.getClass())) {
                    continue;
                }
                try {
                    method.invoke(Listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void subscribeListener(Listener listener) {
        listeners.add(listener);
    }
}
