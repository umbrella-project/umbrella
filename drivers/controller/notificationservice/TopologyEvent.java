package drivers.controller.notificationservice;

import api.notificationservice.Event;
import api.topostore.TopoEdge;
import api.topostore.TopoVertex;

public class TopologyEvent extends Event {
    protected TopologyEventType topologyEventType;

    protected TopoVertex vertex;
    protected TopoEdge edge;

    public TopologyEvent(TopologyEventType eventType, TopoVertex vertex) {
        this.topologyEventType = eventType;
        this.vertex = vertex;
    }

    public TopologyEvent(TopologyEventType eventType, TopoEdge edge) {
        this.topologyEventType = eventType;
        this.edge = edge;
    }

    public TopoEdge getEdge() {
        return this.edge;
    }

    public TopologyEventType getTopologyEventType() {
        return this.topologyEventType;
    }

    public TopoVertex getVertex() {
        return this.vertex;
    }
}
