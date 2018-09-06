Firewall Application
~~~~~~~~~~~~~~~~~~~~~


* Suppose we would like to implement a simple firewall application  to enforce the following access control policy for a network topology that all of the hosts belong to the same subnet. 


+--------------+-----------------+-----------------+-----------------+-----------------+
|              | H1 (10.0.0.1)   | (H2) 10.0.0.2   | (H3) 10.0.0.3   | H4(10.0.0.4)    |
+==============+=================+=================+=================+=================+
| H1(10.0.0.1) | NONE            | DENY            | WEB             | DENY            | 
+--------------+-----------------+-----------------+-----------------+-----------------+
| H2(10.0.0.2) | DENY            | NONE            | DNEY            | PING            |
+--------------+-----------------+-----------------+-----------------+-----------------+
| H3(10.0.0.3) | WEB             | DENY            | NONE            | DENY            |
+--------------+-----------------+-----------------+-----------------+-----------------+
| H4(10.0.0.4) | DENY            | DENY            | NONE            | NONE            | 
+--------------+-----------------+-----------------+-----------------+-----------------+

* To achieve that goal, we implement an application using Umbrella APIs to generate and install appropriate forwarding rules on the switches between each pair of hosts: (h1,h3) and (h2, h4). We explain step by step how to write the mentioned application:

First, we need to create an instance of the controller that we would like to execute our application on. We assume that the name of controller is stored in a config file (i.e. config.properties) and we use it to initialize the controller. 

.. code-block:: java 
        
    String controllerName;
    Controller controller = null;
    ConfigService configService = new ConfigService();
    controllerName = configService.getControllerName();
    controller = configService.init(controllerName);



2. Second, we need to get the list of currents hosts and find a shortest path for each pair of hosts: (h1, h3) and (h2,h4). 

3. Third, we generate appropriate match-action fields for each pair of hosts based on the type of traffic which is specified in the table above. For the pair (h1, h3), we should use source and destination TCP ports (i.e. port 80 for web traffic) to route web traffic and for the pair (h2, h4) we should use ICMP type and code match fields (i.e. type=8 (echo request), type=0 (echo reply) and code=0) to route ICMP traffic. Note that for both types of traffic, we also use source and destination MAC addresses, source and destination IP addresses, Ethernet type, and IP protocol number as the match fields. 

.. code-block:: java

        Set<TopoHost> srchosts = controller.topoStore.getHosts();

        ArrayList<TopoHost> hosts = new ArrayList<>(srchosts);

        List<TopoEdge> fwPath = null;
        List<TopoEdge> rvPath = null;


       for (TopoHost srcHost: hosts) {
            for (TopoHost dstHost: hosts) {
                       if(!srcHost.equals(dstHost)){
            
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



Testing the Forwarding Application on ONOS controller
------------------------------------------------------
* In this section, we explain a Mininet simulation scenario that can be used to test the forwarding application on ONOS controller:

1. First, you need to install and run ONOS on your local machine using the guidelines that have been posted on ONOS website: `ONOS GUIDES`_

2. Second, you need to download and install Mininet using the guidelines that have been posted on Mininet website: `Mininet`_

3. Third, execute the following commands to run a Mininet simulation scenario that simulates a leaf-spine network topology with 6 hosts::
   
         $ cd mininet_examples
         $ sudo mn --topo tree,depth=2,fanout=2 --controller=remote,ip=127.0.0.1,port=6653
    
   
4. Forth, run pingall to detect all of the hosts in the network topology. ONOS runs a reactive forwarding application by default. 

4. Fifth, package umbrella source codes using the following command::

        $ mvn package
   
5. Finally, execute the application using the following command to install rules on network switches::

        $java -cp target/umbrella-1.0-SNAPSHOT-jar-with-dependencies.jar apps.Firewall

Testing the Forwarding Application on OpenDayLight controller
-------------------------------------------------------------
1. First, you need to install and run ONOS on your local machine using the guidelines that have been posted on ONOS website: `ODL GUIDES`_

2. Second, you need to download and install Mininet using the guidelines that have been posted on Mininet website: `Mininet`_

3. Third, execute the following commands to run a Mininet simulation scenario that simulates a leaf-spine network topology with 6 hosts::
   
         $ cd mininet_examples
         $ sudo mn --topo tree,depth=2,fanout=2 --controller=remote,ip=127.0.0.1,port=6653
    
   
4. Forth, run pingall to detect all of the hosts in the network topology. ONOS runs a reactive forwarding application by default. 

4. Fifth, package umbrella source codes using the following command::

        $ mvn package
   
5. Finally, execute the application using the following command to install rules on network switches::

        $java -cp target/umbrella-1.0-SNAPSHOT-jar-with-dependencies.jar apps.Firewall






.. _Mininet: http://mininet.org/download/
.. _ONOS GUIDES: https://wiki.onosproject.org/display/ONOS/Guides
.. _ODL GUIDES: http://docs.opendaylight.org/en/stable-oxygen/getting-started-guide/installing_opendaylight.html
