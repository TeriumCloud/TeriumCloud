package cloud.terium.teriumapi.event;

public interface Listener {

    /**
     * Override this with your own logic e.g. if you want to toggle listeners during runtime.
     *
     * @return Boolean when the listener is active true else false
     */
    default boolean isActive() {
        return true;
    }

}
