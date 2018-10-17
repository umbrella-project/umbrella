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
import api.notificationservice.Event;
import api.notificationservice.EventListener;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoSwitch;
import api.topostore.TopoVertex;
import apps.tests.TestPacketIn;
import config.ConfigService;
import drivers.controller.Controller;
import drivers.controller.packetService.PacketInEvent;
import drivers.controller.packetService.PacketInEventMonitor;
import org.apache.log4j.Logger;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.onlab.packet.ARP;
import org.onlab.packet.Ethernet;
import org.onlab.packet.IPv4;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import utility.DefaultRestApiHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Reactive FwdWithFailureDetection Application.
 */

public class ReactiveFwdRyu {

    private static Logger log = Logger.getLogger(TestPacketIn.class);
    ConfigService configService = new ConfigService();

    private static int TABLE_ID = 100;
    private static int TABLE_ID_CTRL_PACKETS = 200;
    public static void main(String[] args) {


        String controllerName;
        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();
        controller = configService.init(controllerName);
        PacketInEventMonitor packetInEventMonitor = new PacketInEventMonitor(controller, Integer.parseInt(args[0]));
        Controller finalController = controller;
        Set<TopoSwitch> topoSwitches = finalController.topoStore.getSwitches();

        for (TopoSwitch topoSwitch : topoSwitches) {
	    String deviceId = String.valueOf(Long.parseLong(topoSwitch.getSwitchID(), 16));
            FlowMatch flowMatch = FlowMatch.builder()
                    .ethType(2048)
                    .build();

            FlowAction flowAction = new FlowAction(FlowActionType.CONTROLLER,
                    0);

            ArrayList<FlowAction> flowActions = new ArrayList<FlowAction>();
            flowActions.add(flowAction);

            Flow flow = Flow.builder()
                    .deviceID(deviceId)
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
                    .deviceID(deviceId)
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


        class PacketInEventListener extends EventListener {

            DefaultRestApiHelper restApiHelper = new DefaultRestApiHelper();

            KruskalMinimumSpanningTree<TopoVertex,TopoEdge> spanningTree;


            @Override
            public void onEvent(Event event) {

                spanningTree = new KruskalMinimumSpanningTree<>(finalController.topoStore.getGraph());

                PacketInEvent packetInEvent = (PacketInEvent) event;

                Set<TopoHost> topoHosts = finalController.topoStore.getHosts();

                switch (packetInEvent.getPacketEventType()) {
                    case PACKET_IN_EVENT:

                        OFPacketIn ofPacketIn = packetInEvent.getOfPacketIn();
                        OFFactory myFactory = OFFactories.getFactory(ofPacketIn.getVersion());

                        Ethernet eth = packetInEvent.parsed();

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



                            if (!finalController.topoStore.checkHostExistenceWithIP(targetIpAddress)) {
                                Set<TopoEdge> topoEdgeSetSpt = spanningTree.getSpanningTree().getEdges();


                                return;
                            }



                            String dstMac = finalController.topoStore.getTopoHostByIP(targetIpAddress).getHostMac();

                            if (dstMac == null) {



                                return;
                            }

                            Ethernet ethReply = ARP.buildArpReply(targetIpAddress,
                                    MacAddress.valueOf(dstMac),
                                    eth);

                            OFActionOutput output = myFactory.actions().buildOutput()
                                    .setPort(OFPort.ofInt(packetInEvent.getInPortNum()))
                                    .build();

                            OFPacketOut packetOut = myFactory.buildPacketOut()
                                    .setData(ethReply.serialize())
                                    .setBufferId(OFBufferId.NO_BUFFER)
                                    .setInPort(OFPort.ANY)
                                    .setActions(Collections.singletonList((OFAction) output))
                                    .build();



                            try {
                                restApiHelper.httpPostRequest(configService.getApiOn(),
                                        packetInEvent.getDpidNum(),
                                        packetOut);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
                                        .ipv4Src(srcHost.getHostIPAddresses().get(0))
                                        .ipv4Dst(dstHost.getHostIPAddresses().get(0))
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
					.isPermanent(false)
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
                                        .ipv4Src(dstHost.getHostIPAddresses().get(0))
                                        .ipv4Dst(srcHost.getHostIPAddresses().get(0))
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
					.isPermanent(false)
                                        .build();

                                finalController.flowService.addFlow(flow);
                            }

                            int packetOutPort = Integer.parseInt(firstEdge.getSrcPort());

                            OFActionOutput output = myFactory.actions().buildOutput()
                                    .setPort(OFPort.ofInt(packetOutPort))
                                    .build();
                            OFPacketOut packetOut = myFactory.buildPacketOut()
                                    .setData(eth.serialize())
                                    .setBufferId(OFBufferId.NO_BUFFER)
                                    .setInPort(OFPort.ANY)
                                    .setActions(Collections.singletonList((OFAction) output))
                                    .build();




                            try {
                                restApiHelper.httpPostRequest(configService.getApiOn(), packetInEvent.getDpidNum(), packetOut);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }

            }
        }

        PacketInEventListener packetInEventListener = new PacketInEventListener();

        packetInEventMonitor.addEventListener(packetInEventListener);
    }
}
