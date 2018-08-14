package apps.tests;

import api.notificationservice.Event;
import api.notificationservice.EventListener;
import drivers.controller.Controller;
import drivers.controller.notificationservice.TopologyEvent;
import drivers.controller.notificationservice.TopologyEventMonitor;
import drivers.onos.OnosController;

public class TestOnosTopologyNotification {

    public static void main(String[] args) {

        OnosController onosController = new OnosController();

        onosController.printTopology();

        TopologyEventMonitor topologyEventMonitor = new TopologyEventMonitor((Controller) onosController);

        class OnosTopologyEventListener extends EventListener {
            public void onEvent(Event event) {
                System.out.println("ONOS Topology Event");
                TopologyEvent topologyEvent = (TopologyEvent) event;

                switch (topologyEvent.getTopologyEventType()) {
                    case NODE_UP:
                        System.out.println("NODE UP: " + topologyEvent.getVertex().getID());
                        break;

                    case NODE_DOWN:
                        System.out.println("NODE DOWN: " + topologyEvent.getVertex().getID());
                        break;

                    case LINK_UP:
                        System.out.println("LINK UP " + topologyEvent.getEdge().getSrc() + " -> " + topologyEvent.getEdge().getDst());
                        break;

                    case LINK_DOWN:
                        System.out.println("LINK DOWN " + topologyEvent.getEdge().getSrc() + " -> " + topologyEvent.getEdge().getDst());
                        break;
                }
            }
        }

        OnosTopologyEventListener eventListener = new OnosTopologyEventListener();

        topologyEventMonitor.addEventListener(eventListener);

        while (true) {
            ;
        }
    }
}
