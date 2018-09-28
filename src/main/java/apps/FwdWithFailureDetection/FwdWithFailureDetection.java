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

package apps.FwdWithFailureDetection;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import api.notificationservice.Event;
import api.notificationservice.EventListener;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import config.ConfigService;
import drivers.controller.Controller;
import drivers.controller.notificationservice.TopologyEvent;
import drivers.controller.notificationservice.TopologyEventMonitor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FwdWithFailureDetection {

    private static Logger log = Logger.getLogger(FwdWithFailureDetection.class);


    public static void main(String[] args) {



        String controllerName;
        Controller controller;

        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();
        controller = configService.init(controllerName);

        TopologyEventMonitor topologyEventMonitor = new TopologyEventMonitor(controller);
        FlowRuleManager flowRuleManager = new FlowRuleManager();
        flowRuleManager.installRules();


        class TopologyEventListener extends EventListener {
            public void onEvent(Event event) {

                TopologyEvent topologyEvent = (TopologyEvent) event;

                switch (topologyEvent.getTopologyEventType()) {


                    case LINK_UP:
                        log.info("LINK UP " + topologyEvent.getEdge().getSrc() + " -> "
                                + topologyEvent.getEdge().getDst() + "\n");
                        flowRuleManager.updateRules(topologyEvent.getEdge());
                        break;

                    case LINK_DOWN:
                        log.info("LINK DOWN " + topologyEvent.getEdge().getSrc() + " -> "
                                + topologyEvent.getEdge().getDst() + "\n");
                        flowRuleManager.updateRules(topologyEvent.getEdge());
                        break;
                }

            }
        }

        TopologyEventListener eventListener = new TopologyEventListener();

        topologyEventMonitor.addEventListener(eventListener);

        while (true) {

            ;
        }


    }
}
