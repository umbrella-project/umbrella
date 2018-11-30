package drivers.controller.packetService;

import api.notificationservice.Event;
import api.topostore.TopoSwitch;
import org.onlab.packet.DeserializationException;
import org.onlab.packet.Ethernet;
import org.projectfloodlight.openflow.protocol.OFPacketIn;

import java.nio.ByteBuffer;

public class PacketInEvent extends Event {

    protected PacketEventType packetEventType;
    byte[] dpid;
    byte[] inPort;
    private OFPacketIn ofPacketIn;


    public PacketInEvent(PacketEventType packetEventType,
                         OFPacketIn ofPacketIn,
                         byte[] dpid,
                         byte[] inPort) {
        this.ofPacketIn = ofPacketIn;
        this.packetEventType = packetEventType;
        this.dpid = dpid;
        this.inPort = inPort;

    }

    public int getInPortNum() {

        ByteBuffer portWrapped = ByteBuffer.wrap(inPort);
        int inPortNum = portWrapped.getInt();

        return inPortNum;
    }

    public long getDpidNum() {
        ByteBuffer dpidWrapped = ByteBuffer.wrap(dpid); // big-endian by default
        long dpidNum = dpidWrapped.getLong();
        return dpidNum;
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
