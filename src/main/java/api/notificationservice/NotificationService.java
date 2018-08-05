package api.notificationservice;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    List<EventMonitor> eventMonitors;

    NotificationService() {
        eventMonitors = new ArrayList<EventMonitor>();
    }

    void addEventMonitor(EventMonitor eventMonitor) {
        this.eventMonitors.add(eventMonitor);
    }
}
