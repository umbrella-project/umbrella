Forwarding Application 
~~~~~~~~~~~~~~~~~~~~~~~

Suppose we would like to write an application to route traffic between all of the hosts that belong to the same subnet. To achieve this goal, we implement an application using Umbrella APIs to generate and install appropriate forwarding rules on the switches between each two hosts. We explain step by step how to write the mentioned application:

1. First, we need to create an instance of the controller that we would like to execute our application on. We assume that the name of controller is stored in a config file (i.e. config.properties) and we use it to initialize the controller. 

.. code-block:: java 
        
    String controllerName;
    Controller controller = null;
    ConfigService configService = new ConfigService();
    controllerName = configService.getControllerName();
    controller = configService.init(controllerName);

2. Second, we need to get the list of current hosts that have been detected by the controller.

.. code-block:: java

    Set<TopoHost> srchosts = controller.getHosts();
    Set<TopoHost> dsthosts = controller.getHosts();

3. Third, we need to do the following steps:


    * We compute shortest path between each two hosts to determine which network switches should be configured between the given hosts.
    * We generate flow rules based on source and destination MAC addresses, source and destination IP addresses, and Ethernet type (IPv4 in this example).
    * Finally, we install generated flow rules on determined network switches.


.. code-block:: java

    for(TopoHost srcHost: srchosts)
        {
            for(TopoHost dstHost: dsthosts)
            {
                if(!srcHost.equals(dstHost))
                {
                    String srcMac = srcHost.getHostMac();
                    String dstMac = dstHost.getHostMac();


                    path = controller.getShortestPath(srcHost.getHostID(), 
                                                            dstHost.getHostID());
                    controller.printPath(path);

                    for(TopoEdge edge : path) {

                        if(edge.getType() == TopoEdgeType.HOST_SWITCH) {
                            continue;
                        }

                        FlowMatch flowMatch = FlowMatch.builder()
                                .ethSrc(srcMac)
                                .ethDst(dstMac)
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
                                .tableID(0)
                                .flowMatch(flowMatch)
                                .flowActions(flowActions)
                                .priority(1000)
                                .appId("TestForwarding")
                                .timeOut(300)
                                .build();

                        controller.flowService.addFlow(flow);
                    }


                }
            }
        }


Testing the Forwarding Application on ONOS controller
-----------------------------------------------------
* In this section, we explain a Mininet simulation scenario that can be used to test the forwarding application on ONOS controller:

1. First, you need to install and run ONOS on your local machine using the guidelines that have been posted on ONOS website: `ONOS GUIDES`_

2. Second, you need to download and install Mininet using the guidelines that have been posted on Mininet website: `Mininet`_

3. Third, execute the following commands to run a Mininet simulation scenario that simulates tree topology with 8 hosts::

         $ sudo mn --topo tree,depth=2,fanout=8 --controller=remote,ip=127.0.0.1,port=6653
    
   
4. Forth, run pingall to detect all of the hosts in the network topology. ONOS runs a reactive forwarding application by default. 

4. Fifth, package umbrella source codes using the following command::

        $ mvn package
   
5. Finally, execute the application using the following command to install rules on network switches::

        $java -cp target/umbrella-1.0-SNAPSHOT-jar-with-dependencies.jar apps.Forwarding

Testing the Forwarding Application on OpenDayLight controller
-------------------------------------------------------------
1. First, you need to install and run ONOS on your local machine using the guidelines that have been posted on ONOS website: `ODL GUIDES`_

2. Second, you need to download and install Mininet using the guidelines that have been posted on Mininet website: `Mininet`_

3. Third, execute the following commands to run a Mininet simulation scenario that simulates a tree topology with 8 hosts::
   
         $ sudo mn --topo tree,depth=2,fanout=8 --controller=remote,ip=127.0.0.1
    
   
4. Forth, run pingall to detect all of the hosts in the network topology. ONOS runs a reactive forwarding application by default. 

4. Fifth, package umbrella source codes using the following command::

        $ mvn package
   
5. Finally, execute the application using the following command to install rules on network switches::

        $java -cp target/umbrella-1.0-SNAPSHOT-jar-with-dependencies.jar apps.Forwarding

.. _Mininet: http://mininet.org/download/
.. _ONOS GUIDES: https://wiki.onosproject.org/display/ONOS/Guides
.. _ODL GUIDES: http://docs.opendaylight.org/en/stable-oxygen/getting-started-guide/installing_opendaylight.html

