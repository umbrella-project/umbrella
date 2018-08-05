package api.notificationservice;

import drivers.controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class EventMonitor {

    public List<EventListener> eventListeners;
    protected Controller controller;

    public EventMonitor(Controller controller) {
        this.controller = controller;
        eventListeners = new ArrayList<>();
    }

    public EventMonitor() {

    }

    public void addEventListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    void eventThread() {

    }
}
