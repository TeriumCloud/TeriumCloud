package cloud.terium.teriumapi.event;

public interface IEventManager {

    /**
     * Use that methode to call a custom event.
     *
     * @param event Event
     * @param <T> T
     */
    <T extends Event> void callEvent(T event);

    /**
     * Use this methode to register a cloud listener.
     *
     * @param listener Listener
     */
    void registerListener(Listener listener);
}