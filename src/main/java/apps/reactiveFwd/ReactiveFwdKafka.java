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

package apps.reactiveFwd;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoSwitch;
import apps.tests.TestPacketIn;
import com.google.protobuf.InvalidProtocolBufferException;
import config.ConfigService;
import core.notificationService.eventConsumerService.EventConsumerService;
import core.notificationService.packetService.PacketEventMonitor;
import drivers.controller.Controller;
import drivers.controller.packetService.PacketInEventMonitor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Bytes;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.onlab.packet.*;
import org.onosproject.grpc.net.packet.models.InboundPacketProtoOuterClass;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass;
import org.projectfloodlight.openflow.protocol.*;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import restapihelper.JsonBuilder;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Reactive FwdWithFailureDetection Application.
 */

public class ReactiveFwdKafka {

    private static Logger log = Logger.getLogger(TestPacketIn.class);
    private static int TABLE_ID = 0;
    private static int TABLE_ID_CTRL_PACKETS = 0;
    ConfigService configService = new ConfigService();

    public static void main(String[] args) {


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
                    .priority(100)
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
                    .priority(100)
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
        JSONObject registerReponse = jsonBuilder.createJsonObject(PacketsBufferReader);
        monitor.kafkaSubscribe(EventType, appName, registerReponse);
        PacketEventMonitor packetInEventMonitor = new PacketEventMonitor(registerReponse, EventType);



        class PacketInEventListener extends api.eventServiceApi.EventListener {

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

                OFFactory myFactory = OFFactories.getFactory(OFVersion.OF_13);


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

                    log.info("ARP PACKET\n");


                    String dstMac = finalController.topoStore.getTopoHostByIP(targetIpAddress).getHostMac();

                    if (dstMac == null) {


                        return;
                    }

                    Ethernet ethReply = ARP.buildArpReply(targetIpAddress,
                            MacAddress.valueOf(dstMac),
                            eth);

                    /*OFActionOutput output = myFactory.actions().buildOutput()
                            .setPort(OFPort.ofInt(Integer.parseInt(inboundPacketProto.getConnectPoint().getPortNumber())))
                            .build();

                    OFPacketOut packetOut = myFactory.buildPacketOut()
                            .setData(ethReply.serialize())
                            .setBufferId(OFBufferId.NO_BUFFER)
                            .setInPort(OFPort.ANY)
                            .setActions(Collections.singletonList((OFAction) output))
                            .build();


                    try {
                        restApiHelper.httpPostRequest(configService.getApiOn(),
                                Long.parseLong(inboundPacketProto.getConnectPoint().getDeviceId()),
                                packetOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    return;

                }


                if (type == Ethernet.TYPE_IPV4) {

                    log.info("IP Packet\n");
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
                                .priority(1000)
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
                                .priority(1000)
                                .appId("ReactiveFwd")
                                .timeOut(50)
                                .build();

                        finalController.flowService.addFlow(flow);
                    }

                    int packetOutPort = Integer.parseInt(firstEdge.getSrcPort());

                    /*OFActionOutput output = myFactory.actions().buildOutput()
                            .setPort(OFPort.ofInt(packetOutPort))
                            .build();
                    OFPacketOut packetOut = myFactory.buildPacketOut()
                            .setData(eth.serialize())
                            .setBufferId(OFBufferId.NO_BUFFER)
                            .setInPort(OFPort.ANY)
                            .setActions(Collections.singletonList((OFAction) output))
                            .build();


                    try {
                        restApiHelper.httpPostRequest(configService.getApiOn(),
                                Long.parseLong(inboundPacketProto.getConnectPoint().getDeviceId()), packetOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }


            }
        }


        PacketInEventListener packetInEventListener = new PacketInEventListener();
        packetInEventMonitor.addEventListener(packetInEventListener);
        Thread t = new Thread(packetInEventMonitor);
        t.start();


    }
}
