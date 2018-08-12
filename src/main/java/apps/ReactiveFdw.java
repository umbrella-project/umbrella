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

package apps;

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
import config.ConfigService;
import drivers.controller.Controller;
import drivers.controller.packetService.PacketInEvent;
import drivers.controller.packetService.PacketInEventMonitor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.onlab.packet.ARP;
import org.onlab.packet.Ethernet;
import org.onlab.packet.IPv4;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFRequest;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import sun.net.www.protocol.http.HttpURLConnection;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ReactiveFdw {

    private static Logger log = Logger.getLogger(TestPacketIn.class);

    public static void main(String[] args) {


        String controllerName;
        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();
        controller = configService.init(controllerName);
        PacketInEventMonitor packetInEventMonitor = new PacketInEventMonitor(controller, Integer.parseInt(args[0]));
        Controller finalController = controller;
        Set<TopoSwitch> topoSwitches = finalController.topoStore.getSwitches();

        for(TopoSwitch topoSwitch: topoSwitches)
        {
            FlowMatch flowMatch = FlowMatch.builder()

                    .ethType(2048)
                    .build();

            FlowAction flowAction = new FlowAction(FlowActionType.CONTROLLER,
                    0);

            ArrayList<FlowAction> flowActions = new ArrayList<FlowAction>();
            flowActions.add(flowAction);

            Flow flow = Flow.builder()
                    .deviceID(topoSwitch.getSwitchID())
                    .tableID(0)
                    .flowMatch(flowMatch)
                    .flowActions(flowActions)
                    .priority(100)
                    .appId("TestForwarding")
                    .isPermanent(true)
                    .build();

            finalController.flowService.addFlow(flow);

        }



        class PacketInEventListener extends EventListener {

           DefaultRestApiHelper restApiHelper = new DefaultRestApiHelper();


            @Override
            public void onEvent(Event event) {

                PacketInEvent packetInEvent = (PacketInEvent) event;

                Set<TopoHost> topoHosts = finalController.topoStore.getHosts();

                switch (packetInEvent.getPacketEventType()) {
                    case PACKET_IN_EVENT:

                        OFPacketIn ofPacketIn = packetInEvent.getOfPacketIn();
                        OFFactory myFactory = OFFactories.getFactory(ofPacketIn.getVersion());

                        Ethernet eth = packetInEvent.parsed();

                        if(eth == null)
                        {
                            return;
                        }

                        long type = eth.getEtherType();


                        if(type == Ethernet.TYPE_ARP)
                        {
                            ARP arpPacket = (ARP) eth.getPayload();
                            Ip4Address targetIpAddress = Ip4Address
                                    .valueOf(arpPacket.getTargetProtocolAddress());

                            log.info("ARP PACKET\n");
                            OFActionOutput output = myFactory.actions().buildOutput()
                                    .setPort(OFPort.ofInt(packetInEvent.getInPortNum()))
                                    .build();


                            log.info(finalController.topoStore.checkHostExistenceWithIP(targetIpAddress));

                            if(!finalController.topoStore.checkHostExistenceWithIP(targetIpAddress))
                            {
                                return;
                            }

                            //log.info("Target IP address exists\n");

                            String dstMac = finalController.topoStore.getTopoHostByIP(targetIpAddress).getHostMac();

                            if(dstMac ==null)
                            {
                                return;
                            }

                            //log.info("dst mac:" + dstMac + "\n");
                            Ethernet ethReply = ARP.buildArpReply(targetIpAddress,
                                    MacAddress.valueOf(dstMac),
                                    eth);


                            OFPacketOut packetOut = myFactory.buildPacketOut()
                                    .setData(ethReply.serialize())
                                    .setBufferId(OFBufferId.NO_BUFFER)
                                    .setInPort(OFPort.ANY)
                                    .setActions(Collections.singletonList((OFAction) output))
                                    .build();



                            //log.info("dpid:" + packetInEvent.getDpidNum() + " " +"inport:" + packetInEvent.getInPortNum() + "\n");
                            try {
                                restApiHelper.httpPostRequest(8005, packetInEvent.getDpidNum(), packetOut);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return;


                        }


                        if(type == Ethernet.TYPE_IPV4) {

                            log.info("IP Packet\n");
                            IPv4 IPv4packet = (IPv4) eth.getPayload();


                            if(!finalController.topoStore.checkHostExistenceWithMac(eth.getSourceMAC())
                                 || !finalController.topoStore.checkHostExistenceWithMac(eth.getDestinationMAC()) )
                            {
                                return;

                            }



                            TopoHost srcHost = finalController.topoStore.getTopoHostByMac(eth.getSourceMAC());
                            TopoHost dstHost = finalController.topoStore.getTopoHostByMac(eth.getDestinationMAC());

                            log.info(srcHost.getHostID() + ":" + dstHost.getHostID() + "\n");
                            if(srcHost == null || dstHost == null)
                            {
                                return;
                            }


                            List<TopoEdge> path = null;


                            // Forward Path

                            path = finalController.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());


                            for (TopoEdge edge : path) {

                                if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                                    continue;
                                }

                                FlowMatch flowMatch = FlowMatch.builder()
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
                                        .tableID(0)
                                        .flowMatch(flowMatch)
                                        .flowActions(flowActions)
                                        .priority(1000)
                                        .appId("TestForwarding")
                                        .timeOut(50)
                                        .build();

                                finalController.flowService.addFlow(flow);
                            }


                            // Reverse Path
                            path = finalController.topoStore.getShortestPath(dstHost.getHostID(), srcHost.getHostID());
                            //finalController.printPath(path);

                            for (TopoEdge edge : path) {

                                if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                                    continue;
                                }

                                FlowMatch flowMatch = FlowMatch.builder()
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
                                        .tableID(0)
                                        .flowMatch(flowMatch)
                                        .flowActions(flowActions)
                                        .priority(1000)
                                        .appId("TestForwarding")
                                        .timeOut(50)
                                        .build();

                                finalController.flowService.addFlow(flow);
                            }

                        }
                }

            }
        }

        PacketInEventListener packetInEventListener = new PacketInEventListener();

        packetInEventMonitor.addEventListener(packetInEventListener);
    }
}
