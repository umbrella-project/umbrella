/*
 * Copyright ${2018} [Adib Rastegarnia, Rajas H.Karandikar, Douglas Comer]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package apps.reactiveForwardingKafka;

import api.eventServiceApi.EventListener;
import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoSwitch;

import com.google.protobuf.ByteString;
import config.ConfigService;
import core.notificationService.eventConsumerService.EventConsumerService;
import core.notificationService.packetService.PacketEventMonitor;

import drivers.controller.Controller;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Bytes;
//import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.onlab.packet.*;
import org.onosproject.grpc.net.flow.instructions.models.InstructionProtoOuterClass;

import org.onosproject.grpc.net.flow.models.TrafficTreatmentProtoOuterClass;
import org.onosproject.grpc.net.models.PortProtoOuterClass;
import org.onosproject.grpc.net.packet.models.InboundPacketProtoOuterClass;
import org.onosproject.grpc.net.packet.models.OutboundPacketProtoOuterClass;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass;
import org.onosproject.grpc.net.packet.models.PacketOutServiceGrpc;
import tools.utility.DefaultRestApiHelper;
import tools.utility.JsonBuilder;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Reactive FwdWithFailureDetection Application.
 */

public class ReactiveForwardingKafka {

    //private static Logger log = Logger.getLogger(ReactiveForwardingKafka.class);
    private static int TABLE_ID = 0;
    private static int TABLE_ID_CTRL_PACKETS = 0;
    private static int CTRL_PACKET_PRIORITY = 100;
    private static int IP_PACKET_PRIORITY = 1000;
    ConfigService configService = new ConfigService();



    public ReactiveForwardingKafka() {

    }


    public static void main(String[] args) {


        ManagedChannel channel;

        PacketOutServiceGrpc.PacketOutServiceBlockingStub packetOutServiceBlockingStub;
        channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051)
                .usePlaintext()
                .build();

        packetOutServiceBlockingStub = PacketOutServiceGrpc.newBlockingStub(channel);



        String controllerName;
        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();
        controller = configService.init(controllerName);
        Controller finalController = controller;
        Set<TopoSwitch> topoSwitches = finalController.topoStore.getSwitches();

        for (TopoSwitch topoSwitch : topoSwitches) {
            FlowMatch flowMatch = FlowMatch.builder()
                    .ethType(2048)
                    .build();

            FlowAction flowAction = new FlowAction(FlowActionType.CONTROLLER,
                    0);

            ArrayList<FlowAction> flowActions = new ArrayList<FlowAction>();
            flowActions.add(flowAction);

            Flow flow = Flow.builder()
                    .deviceID(topoSwitch.getSwitchID())
                    .tableID(TABLE_ID_CTRL_PACKETS)
                    .flowMatch(flowMatch)
                    .flowActions(flowActions)
                    .priority(CTRL_PACKET_PRIORITY)
                    .appId("ReactiveFwd")
                    .isPermanent(true)
                    .timeOut(0)
                    .build();

            finalController.flowService.addFlow(flow);

            flowMatch = FlowMatch.builder()
                    .ethType(0x0806)
                    .build();

            flow = Flow.builder()
                    .deviceID(topoSwitch.getSwitchID())
                    .tableID(TABLE_ID_CTRL_PACKETS)
                    .flowMatch(flowMatch)
                    .flowActions(flowActions)
                    .priority(CTRL_PACKET_PRIORITY)
                    .appId("ReactiveFwd")
                    .isPermanent(true)
                    .timeOut(0)
                    .build();

            finalController.flowService.addFlow(flow);
        }

        String appName = "PacketConsumerApp";
        String EventType = "PACKET";
        JsonBuilder jsonBuilder = new JsonBuilder();
        EventConsumerService monitor = new EventConsumerService();
        BufferedReader PacketsBufferReader = monitor.kafkaRegister(appName);
        JSONObject registerReponse = jsonBuilder.createJsonArray(PacketsBufferReader);
        monitor.kafkaSubscribe(EventType, appName, registerReponse);
        PacketEventMonitor packetInEventMonitor = new PacketEventMonitor(registerReponse, EventType);


        class PacketInEventListener extends EventListener {

            DefaultRestApiHelper restApiHelper = new DefaultRestApiHelper();

            @Override
            public void onEvent(ConsumerRecord<Long, Bytes> record) {


                PacketContextProtoOuterClass.PacketContextProto packetContextProto = null;

                try {
                    packetContextProto = PacketContextProtoOuterClass.PacketContextProto.parseFrom(record.value().get());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                PacketContextProtoOuterClass.PacketContextProto finalPacketContextProto = packetContextProto;

                byte[] packetByteArray = finalPacketContextProto.getInboundPacket().getData().toByteArray();

                InboundPacketProtoOuterClass.InboundPacketProto inboundPacketProto = finalPacketContextProto.getInboundPacket();

                Ethernet eth = new Ethernet();
                try {
                    eth = Ethernet.deserializer().deserialize(packetByteArray, 0, packetByteArray.length);
                } catch (DeserializationException e) {
                    e.printStackTrace();
                }

                Set<TopoHost> topoHosts = finalController.topoStore.getHosts();


                if (eth == null) {
                    return;
                }

                long type = eth.getEtherType();


                // Handle ARP packets

                if (type == Ethernet.TYPE_ARP) {
                    ARP arpPacket = (ARP) eth.getPayload();
                    Ip4Address targetIpAddress = Ip4Address
                            .valueOf(arpPacket.getTargetProtocolAddress());




                    String dstMac = finalController.topoStore.getTopoHostByIP(targetIpAddress).getHostMac();

                    if (dstMac == null) {


                        return;
                    }

                    Ethernet ethReply = ARP.buildArpReply(targetIpAddress,
                            MacAddress.valueOf(dstMac),
                            eth);


                    InstructionProtoOuterClass.InstructionProto instructionProto =
                            InstructionProtoOuterClass.InstructionProto.newBuilder().setType(InstructionProtoOuterClass.TypeProto.OUTPUT)
                                    .setPort(PortProtoOuterClass.PortProto
                                            .newBuilder()
                                            .setPortNumber(inboundPacketProto.getConnectPoint().getPortNumber())
                                            .build())
                                    .build();

                    TrafficTreatmentProtoOuterClass.TrafficTreatmentProto trafficTreatmentProto =
                            TrafficTreatmentProtoOuterClass.TrafficTreatmentProto.newBuilder()
                                    .addAllInstructions(instructionProto).build();

                    OutboundPacketProtoOuterClass.OutboundPacketProto outboundPacketProto = OutboundPacketProtoOuterClass.OutboundPacketProto.newBuilder()
                            .setDeviceId(inboundPacketProto.getConnectPoint().getDeviceId())
                            .setTreatment(trafficTreatmentProto)
                            .setData(ByteString.copyFrom(ethReply.serialize()))
                            .build();


                     OutboundPacketProtoOuterClass.PacketOutStatus packetOutStatus =
                             packetOutServiceBlockingStub.emit(outboundPacketProto);



                    return;

                }


                if (type == Ethernet.TYPE_IPV4) {

                    IPv4 IPv4packet = (IPv4) eth.getPayload();
                    byte ipv4Protocol = IPv4packet.getProtocol();


                    if (!finalController.topoStore.checkHostExistenceWithMac(eth.getSourceMAC())
                            || !finalController.topoStore.checkHostExistenceWithMac(eth.getDestinationMAC())) {

                        return;

                    }


                    TopoHost srcHost = finalController.topoStore.getTopoHostByMac(eth.getSourceMAC());
                    TopoHost dstHost = finalController.topoStore.getTopoHostByMac(eth.getDestinationMAC());
                    FlowMatch flowMatchFwd;
                    FlowMatch flowMatchRev;


                    if (srcHost == null || dstHost == null) {
                        return;
                    }


                    List<TopoEdge> path = null;
                    // Forward Path
                    path = finalController.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());
                    TopoEdge firstEdge = path.get(1);
                    for (TopoEdge edge : path) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        flowMatchFwd = FlowMatch.builder()
                                .ethSrc(srcHost.getHostMac())
                                .ethDst(dstHost.getHostMac())
                                .ipv4Src(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ethType(2048)
                                .build();

                        FlowAction flowAction = new FlowAction(FlowActionType.OUTPUT,
                                Integer.parseInt(edge.getSrcPort()));

                        ArrayList<FlowAction> flowActions = new ArrayList<FlowAction>();
                        flowActions.add(flowAction);

                        Flow flow = Flow.builder()
                                .deviceID(edge.getSrc())
                                .tableID(TABLE_ID)
                                .flowMatch(flowMatchFwd)
                                .flowActions(flowActions)
                                .priority(IP_PACKET_PRIORITY)
                                .appId("ReactiveFwd")
                                .timeOut(50)
                                .build();

                        finalController.flowService.addFlow(flow);
                    }


                    // Reverse Path
                    path = finalController.topoStore.getShortestPath(dstHost.getHostID(), srcHost.getHostID());

                    for (TopoEdge edge : path) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        flowMatchRev = FlowMatch.builder()
                                .ethSrc(dstHost.getHostMac())
                                .ethDst(srcHost.getHostMac())
                                .ipv4Src(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ethType(2048)
                                .build();

                        FlowAction flowAction = new FlowAction(FlowActionType.OUTPUT,
                                Integer.parseInt(edge.getSrcPort()));

                        ArrayList<FlowAction> flowActions = new ArrayList<FlowAction>();
                        flowActions.add(flowAction);

                        Flow flow = Flow.builder()
                                .deviceID(edge.getSrc())
                                .tableID(TABLE_ID)
                                .flowMatch(flowMatchRev)
                                .flowActions(flowActions)
                                .priority(IP_PACKET_PRIORITY)
                                .appId("ReactiveFwd")
                                .timeOut(50)
                                .build();

                        finalController.flowService.addFlow(flow);
                    }

                    int packetOutPort = Integer.parseInt(firstEdge.getSrcPort());


                    InstructionProtoOuterClass.InstructionProto instructionProto =
                            InstructionProtoOuterClass.InstructionProto.newBuilder().setType(InstructionProtoOuterClass.TypeProto.OUTPUT)
                                    .setPort(PortProtoOuterClass.PortProto
                                            .newBuilder()
                                            .setPortNumber(firstEdge.getSrcPort())
                                            .build())
                                    .build();

                    TrafficTreatmentProtoOuterClass.TrafficTreatmentProto trafficTreatmentProto =
                            TrafficTreatmentProtoOuterClass.TrafficTreatmentProto.newBuilder()
                                    .addAllInstructions(instructionProto).build();

                    OutboundPacketProtoOuterClass.OutboundPacketProto outboundPacketProto2 =
                            OutboundPacketProtoOuterClass.OutboundPacketProto.newBuilder()
                                    .setDeviceId(inboundPacketProto.getConnectPoint().getDeviceId())
                                    .setTreatment(trafficTreatmentProto)
                                    .setData(inboundPacketProto.getData())
                                    .build();

                    OutboundPacketProtoOuterClass.PacketOutStatus packetOutStatus =
                            packetOutServiceBlockingStub.emit(outboundPacketProto2);

                }


            }
        }


        PacketInEventListener packetInEventListener = new PacketInEventListener();
        packetInEventMonitor.addEventListener(packetInEventListener);
        Thread t = new Thread(packetInEventMonitor);
        t.start();


    }
}
