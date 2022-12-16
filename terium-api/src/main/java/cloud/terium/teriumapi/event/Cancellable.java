package cloud.terium.teriumapi.event;

public abstract class Cancellable extends Event {

    private boolean isCancelled = false;

    /**
     * Get if the event is cancelled. (true if yes else false)
     *
     * @return boolean when the event is cancelled true else false
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Used to cancel the event
     *
     * @param cancelled boolean Change if the event should be cancelled or not.
     */
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}