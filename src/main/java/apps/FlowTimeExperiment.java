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
import api.topostore.TopoSwitch;
import drivers.controller.Controller;
import drivers.odl.OdlController;
import drivers.onos.OnosController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlowTimeExperiment {

    public static void setup(Controller controller) {

        Set<TopoSwitch> switches = controller.topoStore.getSwitches();

        for (TopoSwitch s : switches) {

            FlowMatch flowMatch = FlowMatch.builder()
                    .ethType(2048)
                    .ipProto(0x89)
                    .build();

            FlowAction flowAction = new FlowAction(FlowActionType.DROP);
            ArrayList<FlowAction> flowActions = new ArrayList<>();
            flowActions.add(flowAction);

            Flow flow = Flow.builder()
                    .deviceID(s.getSwitchID())
                    .tableID(0)
                    .priority(500)
                    .timeOut(20)
                    .flowMatch(flowMatch)
                    .flowActions(flowActions)
                    .appId("FlowTimeExp")
                    .build();

            String flowString = controller.flowService.addFlow(flow);
            //System.out.println("Added flow: " + flowString);
        }
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Arguments: onos/odl [number_of_hosts]");
        }

        Controller controller;

        if (args[0].equalsIgnoreCase("onos")) {
            controller = new OnosController();
        } else if (args[0].equalsIgnoreCase("odl")) {
            controller = new OdlController();
        } else {
            return;
        }

        //controller.topoStore.printTopo();

        if (args.length == 1) {
            setup(controller);
            return;
        }

        int lastHost = Integer.parseInt(args[1]);

        String srcMac = "00:00:00:00:00:01";
        //String dstMac = "00:00:00:00:00:" + String.format("%02x", lastHost);
        String dstMac = "00:00:00:00:00:02";

        //System.out.println("Src and Dst " + srcMac + " " + dstMac);

        Set<TopoHost> hosts = controller.topoStore.getHosts();

        TopoHost srcHost = null, dstHost = null;

        for (TopoHost host : hosts) {

            if (host.getHostMac().equalsIgnoreCase(srcMac)) {
                srcHost = host;
            }

            if (host.getHostMac().equalsIgnoreCase(dstMac)) {
                dstHost = host;
            }
        }

        ArrayList<String> flowStrings = new ArrayList<>();

        System.out.println("Searching path from " + srcHost.getHostID() + " " + dstHost.getHostID());
        List<TopoEdge> pathEdges = controller.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());

        for (TopoEdge edge : pathEdges) {

            if (edge.getType() == TopoEdgeType.HOST_SWITCH) {
                continue;
            }

            FlowMatch flowMatch = FlowMatch.builder()
                    .ethSrc(srcMac)
                    .ethDst(dstMac)
                    .ethType(2048)
                    .ipProto(0x89)
                    .build();

            FlowAction flowAction = new FlowAction(FlowActionType.OUTPUT, Integer.parseInt(edge.getSrcPort()));
            ArrayList<FlowAction> flowActions = new ArrayList<>();
            flowActions.add(flowAction);

            Flow flow = Flow.builder()
                    .deviceID(edge.getSrc())
                    .tableID(0)
                    .priority(1000)
                    .timeOut(20)
                    .flowMatch(flowMatch)
                    .flowActions(flowActions)
                    .appId("FlowTimeExp")
                    .build();

            String flowId = controller.flowService.addFlow(flow);

            //System.out.println("Flow added: " + flowId);
        }
    }
}