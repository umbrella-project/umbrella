package apps;

import api.notificationservice.Event;
import api.notificationservice.EventListener;
import drivers.controller.notificationservice.TopologyEvent;
import drivers.odl.OdlController;
import drivers.odl.notificationService.OdlWSTopologyEventMonitor;

public class TestOdlTopologyNotification {

    public static void main(String[] args) {

        OdlController odlController = new OdlController();

        odlController.printTopology();

        //TopologyEventMonitor topologyEventMonitor = new TopologyEventMonitor((Controller)odlController);
        OdlWSTopologyEventMonitor topologyEventMonitor = new OdlWSTopologyEventMonitor(odlController);

        class OdlTopologyEventListener extends EventListener {
            public void onEvent(Event event) {
                System.out.println("ODL Topology Event");
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

        OdlTopologyEventListener eventListener = new OdlTopologyEventListener();

        topologyEventMonitor.addEventListener(eventListener);

        while (true) {
            ;
        }
    }
}
