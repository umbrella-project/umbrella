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
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import config.ConfigService;
import drivers.controller.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Firewall {

    public static void main(String[] args) {

        String controllerName;

        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();

        configService.init(controllerName);


        Set<TopoHost> srchosts = controller.topoStore.getHosts();

        ArrayList<TopoHost> hosts = new ArrayList<>(srchosts);

        List<TopoEdge> fwPath = null;
        List<TopoEdge> rvPath = null;


        for (int i = 0; i < hosts.size(); i++) {
            for (int j = i + 1; j < hosts.size(); j++) {
                TopoHost srcHost = hosts.get(i);
                TopoHost dstHost = hosts.get(j);


                String srcMac = srcHost.getHostMac();
                String dstMac = dstHost.getHostMac();

                String srcIP = srcHost.getHostIPAddresses().get(0);
                String dstIP = dstHost.getHostIPAddresses().get(0);

                fwPath = controller.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());
                rvPath = controller.topoStore.getShortestPath(dstHost.getHostID(), srcHost.getHostID());

                if ((srcIP.equals("10.0.0.1") && dstIP.equals("10.0.0.3"))
                        || (srcIP.equals("10.0.0.3") && dstIP.equals("10.0.0.1"))) {


                    FlowMatch flowMatch = null;

                    for (TopoEdge edge : fwPath) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        flowMatch = FlowMatch.builder()
                                .ethSrc(srcMac)
                                .ethDst(dstMac)
                                .ipv4Src(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ethType(2048)
                                .ipProto(6)
                                .tcpDst(80)
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
                                .priority(100)
                                .appId("Firewall")
                                .timeOut(100)
                                .build();

                        controller.flowService.addFlow(flow);

                    }

                    // Reverse Path

                    for (TopoEdge edge : rvPath) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        flowMatch = FlowMatch.builder()
                                .ethSrc(dstMac)
                                .ethDst(srcMac)
                                .ipv4Src(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ethType(2048)
                                .ipProto(6)
                                .tcpSrc(80)
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
                                .priority(100)
                                .appId("Firewall")
                                .timeOut(100)
                                .build();

                        controller.flowService.addFlow(flow);


                    }


                }

                if ((srcIP.equals("10.0.0.2") && dstIP.equals("10.0.0.4"))
                        || (srcIP.equals("10.0.0.4") && dstIP.equals("10.0.0.2"))) {


                    FlowMatch flowMatch = null;

                    for (TopoEdge edge : fwPath) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }


                        flowMatch = FlowMatch.builder()
                                .ethSrc(srcMac)
                                .ethDst(dstMac)
                                .ipv4Src(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ipProto(0x01)
                                .ethType(2048)
                                .icmpv4_code(0x0)
                                .icmpv4_type(0x08)
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
                                .priority(100)
                                .appId("Firewall")
                                .timeOut(100)
                                .build();

                        controller.flowService.addFlow(flow);


                    }
                    // Reverse Path

                    for (TopoEdge edge : rvPath) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }


                        flowMatch = FlowMatch.builder()
                                .ethSrc(dstMac)
                                .ethDst(srcMac)
                                .ipv4Src(dstHost.getHostIPAddresses().get(0) + "/32")
                                .ipv4Dst(srcHost.getHostIPAddresses().get(0) + "/32")
                                .ipProto(0x01)
                                .ethType(2048)
                                .icmpv4_code(0x0)
                                .icmpv4_type(0x0)
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
                                .priority(100)
                                .appId("Firewall")
                                .timeOut(100)
                                .build();

                        controller.flowService.addFlow(flow);


                    }


                }


            }
        }
    }
}
