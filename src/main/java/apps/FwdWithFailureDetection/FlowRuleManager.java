package apps.FwdWithFailureDetection;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import config.ConfigService;
import drivers.controller.Controller;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlowRuleManager {

    private static Logger log = Logger.getLogger(FlowRuleManager.class);
    private String controllerName;
    private Controller controller;
    private HashMap<TopoEdge, Set<List<TopoEdge>>> edgePathMap = new HashMap<>();
    private HashMap<List<TopoEdge>, List<Flow>> pathFlowsMap = new HashMap<>();
    private HashMap<Flow, String>  flowIdMap = new HashMap<>();
    private  ConfigService configService;

    public FlowRuleManager() {
        configService = new ConfigService();
        controllerName = configService.getControllerName();
        controller = configService.init(controllerName);
    }


    public HashMap<TopoEdge, Set<List<TopoEdge>>> getEdgePathMap() {
        return this.edgePathMap;
    }


    public void updateRules(TopoEdge topoEdge) {

        // Find paths that have been affected because of the failure.
        controller = configService.init(controllerName);
        log.info(controller.topoStore.getTopoEdges().size() + "\n");
        Set<List<TopoEdge>> affectedPaths = edgePathMap.get(topoEdge);


        if (affectedPaths == null) {
            return;
        }

        edgePathMap.remove(topoEdge);

        for (List<TopoEdge> path : affectedPaths) {


            List<Flow> oldFlowRules = pathFlowsMap.get(path);

            for(Flow flow: oldFlowRules)
            {
                String flowID = flowIdMap.get(flow);
                log.info("Device ID:" + flow.getDeviceID() + ", " + flowID + "\n");
                controller.flowService.deleteFlow(flow.getDeviceID(), flowID);
            }


            pathFlowsMap.remove(path);

            List<TopoEdge> newPath = null;

            String srcHostId = path.get(0).getSrc();
            String dstHostId = path.get(path.size() - 1).getDst();

            TopoHost srcTopoHost = controller.topoStore.getTopoHostById(srcHostId);
            TopoHost dstTopoHost = controller.topoStore.getTopoHostById(dstHostId);

            Set<TopoHost> hosts = new HashSet<>();
            hosts.add(srcTopoHost);
            hosts.add(dstTopoHost);

            for (TopoHost srcHost : hosts) {
                for (TopoHost dstHost : hosts) {
                    if (!srcHost.equals(dstHost)) {

                        String srcMac = srcHost.getHostMac();
                        String dstMac = dstHost.getHostMac();


                        newPath = controller.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());


                        for (TopoEdge edge : newPath) {

                            if (edgePathMap.containsKey(edge)) {
                                Set<List<TopoEdge>> paths = edgePathMap.get(edge);
                                paths.add(newPath);
                                edgePathMap.put(edge, paths);
                            } else {
                                Set<List<TopoEdge>> paths = new HashSet<>();
                                paths.add(newPath);
                                edgePathMap.put(edge, paths);
                            }

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
                                    .priority(500)
                                    .appId("ForwardingWithFailOver")
                                    .timeOut(20)
                                    .build();

                            String flowId = controller.flowService.addFlow(flow);
                            flowIdMap.put(flow, flowId);

                            if(pathFlowsMap.containsKey(newPath))
                            {
                                List<Flow> flowsList = pathFlowsMap.get(newPath);
                                flowsList.add(flow);
                                pathFlowsMap.put(newPath, flowsList);



                            }
                            else
                            {
                                List<Flow> flowsList = new ArrayList<>();
                                flowsList.add(flow);
                                pathFlowsMap.put(newPath, flowsList);
                            }

                        }
                    }

                }
            }


        }


    }

    public void installRules() {


        Set<TopoHost> srchosts = controller.topoStore.getHosts();
        Set<TopoHost> dsthosts = controller.topoStore.getHosts();
        List<TopoEdge> path = null;


        for (TopoHost srcHost : srchosts) {
            for (TopoHost dstHost : dsthosts) {
                if (!srcHost.equals(dstHost)) {
                    String srcMac = srcHost.getHostMac();
                    String dstMac = dstHost.getHostMac();


                    path = controller.topoStore.getShortestPath(srcHost.getHostID(), dstHost.getHostID());
                    //controller.printPath(path);


                    for (TopoEdge edge : path) {

                        if (edgePathMap.containsKey(edge)) {
                            Set<List<TopoEdge>> paths = edgePathMap.get(edge);
                            paths.add(path);
                            edgePathMap.put(edge, paths);
                        } else {
                            Set<List<TopoEdge>> paths = new HashSet<>();
                            paths.add(path);
                            edgePathMap.put(edge, paths);
                        }

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
                                .appId("ForwardingWithFailureDetection")
                                .timeOut(20)
                                .build();

                        String flowId = controller.flowService.addFlow(flow);
                        flowIdMap.put(flow, flowId);
                        if(pathFlowsMap.containsKey(path))
                        {
                            List<Flow> flowsList = pathFlowsMap.get(path);

                            flowsList.add(flow);
                            pathFlowsMap.put(path, flowsList);



                        }
                        else
                        {
                            List<Flow> flowsList = new ArrayList<>();
                            flowsList.add(flow);
                            pathFlowsMap.put(path, flowsList);
                        }



                    }


                }
            }
        }


    }


}
