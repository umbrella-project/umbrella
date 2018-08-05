package drivers.controller.notificationservice;

import api.notificationservice.EventListener;
import api.notificationservice.EventMonitor;
import api.topostore.TopoEdge;
import api.topostore.TopoVertex;
import drivers.controller.Controller;
import org.jgrapht.Graph;

public class TopologyEventMonitor extends EventMonitor {

    private int pollingInterval = 1000; // polling interval in ms

    public TopologyEventMonitor(Controller controller) {
        super(controller);
        this.eventThread();
    }

    void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    void eventThread() {

        new Thread(new Runnable() {
            public void run() {

                while (true) {
                    Graph<TopoVertex, TopoEdge> currTopo = controller.topoStore.getGraph();

                    controller.topoStore.updateTopo();

                    for (TopoVertex vertex : currTopo.vertexSet()) {
                        if (!controller.topoStore.getGraph().containsVertex(vertex)) {
                            for (EventListener eventListener : eventListeners) {
                                eventListener.onEvent(new TopologyEvent(TopologyEventType.NODE_DOWN, vertex));
                            }
                        }
                    }

                    for (TopoVertex vertex : controller.topoStore.getGraph().vertexSet()) {
                        if (!currTopo.containsVertex(vertex)) {
                            for (EventListener eventListener : eventListeners) {
                                eventListener.onEvent(new TopologyEvent(TopologyEventType.NODE_UP, vertex));
                            }
                        }
                    }

                    for (TopoEdge edge : currTopo.edgeSet()) {
                        if (!controller.topoStore.getGraph().containsEdge(edge)) {
                            for (EventListener eventListener : eventListeners) {
                                eventListener.onEvent(new TopologyEvent(TopologyEventType.LINK_DOWN, edge));
                            }
                        }
                    }

                    for (TopoEdge edge : controller.topoStore.getGraph().edgeSet()) {
                        if (!currTopo.containsEdge(edge)) {
                            for (EventListener eventListener : eventListeners) {
                                eventListener.onEvent(new TopologyEvent(TopologyEventType.LINK_UP, edge));
                            }
                        }
                    }

                    try {
                        Thread.sleep(pollingInterval);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }

    ;
}
