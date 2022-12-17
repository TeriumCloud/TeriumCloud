package cloud.terium.teriumapi.event;

public interface IEventProvider {

    /**
     * Use that methode to call a custom event.
     *
     * @param event Event
     * @param <T> T
     */
    <T extends Event> void callEvent(T event);

    /**
     * Use this methode to subscribe a cloud listener.
     *
     * @param listener Listener
     */
    void subscribeListener(Listener listener);
}