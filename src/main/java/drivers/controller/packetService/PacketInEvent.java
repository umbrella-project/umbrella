package drivers.controller.packetService;

import api.notificationservice.Event;
import api.topostore.TopoSwitch;
import org.onlab.packet.DeserializationException;
import org.onlab.packet.Ethernet;
import org.projectfloodlight.openflow.protocol.OFPacketIn;

public class PacketInEvent extends Event {

    protected PacketEventType packetEventType;
    private OFPacketIn ofPacketIn;

    public PacketInEvent(PacketEventType packetEventType, OFPacketIn ofPacketIn) {
        this.ofPacketIn = ofPacketIn;
        this.packetEventType = packetEventType;

    }

    public PacketEventType getPacketEventType() {
        return this.packetEventType;
    }

    public OFPacketIn getOfPacketIn() {
        return this.ofPacketIn;
    }

    public Ethernet parsed() {
        Ethernet ethernet = null;

        byte[] data = ofPacketIn.getData();

        try {
            ethernet = Ethernet.deserializer().deserialize(data, 0, data.length);
        } catch (DeserializationException e) {
            e.printStackTrace();
        }


        return ethernet;

    }


    public TopoSwitch receivedFrom() {
        TopoSwitch topoSwitch = new TopoSwitch();


        return topoSwitch;


    }


}
