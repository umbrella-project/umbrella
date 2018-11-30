package api.eventServiceApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification Service.
 */
public class NotificationService {
    /**
     * A List of event monitors to monitor different type of events.
     */
    List<EventMonitor> eventMonitors;

    /**
     * Default constructor.
     */
    NotificationService() {
        eventMonitors = new ArrayList<EventMonitor>();
    }

    /**
     * Adds an event monitor.
     *
     * @param eventMonitor an event monitor.
     */
    void addEventMonitor(EventMonitor eventMonitor) {
        this.eventMonitors.add(eventMonitor);
    }
}

