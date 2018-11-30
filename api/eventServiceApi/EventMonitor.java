package api.eventServiceApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an event monitor.
 */
public class EventMonitor {

    public List<EventListener> eventListeners;


    /**
     * Event monitor constructor.
     */
    public EventMonitor() {
        eventListeners = new ArrayList<>();
    }


    /**
     * Adds an event listener.
     *
     * @param eventListener an event listener.
     */
    public void addEventListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    void eventThread() {

    }
}

