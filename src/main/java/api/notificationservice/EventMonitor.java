package api.notificationservice;

import drivers.controller.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an event monitor.
 */
public class EventMonitor {

    public List<EventListener> eventListeners;
    protected Controller controller;

    /**
     * Event monitor constructor.
     * @param controller a controller instance.
     */
    public EventMonitor(Controller controller) {
        this.controller = controller;
        eventListeners = new ArrayList<>();
    }

    /**
     * Default constructor.
     */
    public EventMonitor() {

    }

    /**
     * Adds an event listener.
     * @param eventListener an event listener.
     */
    public void addEventListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    void eventThread() {

    }
}
