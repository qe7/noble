package sh.squeami.kami.events.api.types;

/**
 * Base class for events that can be cancelled.
 */
public class EventCancellable {

    // Flag to indicate whether the event is cancelled
    private boolean cancelled;

    /**
     * Checks if the event is cancelled.
     *
     * @return True if the event is cancelled, otherwise false.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled status of the event.
     *
     * @param cancelled True to cancel the event, false to allow it.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
