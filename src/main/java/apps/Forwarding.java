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

public class Forwarding {

    public static void main(String[] args) {


        String controllerName;

        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();

        controller = configService.init(controllerName);

        Set<TopoHost> srchosts = controller.topoStore.getHosts();
        Set<TopoHost> dsthosts = controller.topoStore.getHosts();

        List<TopoEdge> path = null;


        for (TopoHost srcHost : srchosts) {
            for (TopoHost dstHost : dsthosts) {
                if (!srcHost.equals(dstHost)) {
                    String srcMac = srcHost.getHostMac();
                    String dstMac = dstHost.getHostMac();


                    path = controller.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());
                    controller.printPath(path);

                    for (TopoEdge edge : path) {

                        if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        FlowMatch flowMatch = FlowMatch.builder()
                                .ethSrc(srcMac)
                                .ethDst(dstMac)
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

                        controller.flowService.addFlow(flow);
                    }


                }
            }
        }
    }
}
