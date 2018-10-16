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

package drivers.ryu.vendors;

import api.flowservice.FlowAction;
import api.flowservice.FlowMatch;
import api.topostore.TopoSwitch;
import config.ConfigService;
import drivers.controller.Controller;
import drivers.ryu.RyuController;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;



public class hpDriver {

    private static Logger log = Logger.getLogger(hpDriver.class);
    public static void main(String[] args) {


        RyuController controller = new RyuController();

        /*-------------------------------------------*/
        /* This part is to delete all previous flows */
        FlowMatch delFlowMatch = FlowMatch.builder().build();

        ArrayList<FlowAction> delFlowActions = new ArrayList<>();

        Set<TopoSwitch> topoSwitchSet =   controller.topoStore.getSwitches();

        for(TopoSwitch topoSwitch:topoSwitchSet)
        {

            log.info(topoSwitch.getSwitchID());

        }

        /*

        Flow delFlow1 = Flow.builder()
                //.deviceID("738997584569600")
                .deviceID("738997585356800")
                .flowMatch(delFlowMatch)
                .flowActions(delFlowActions)
                .tableID(0)
                .priority(0)
                .appId("delFlows")
                .build();
        Flow delFlow2 = Flow.builder()
                //.deviceID("738997584569600")
                .deviceID("738997585356800")
                .flowMatch(delFlowMatch)
                .flowActions(delFlowActions)
                .tableID(100)
                .priority(0)
                .appId("delFlows")
                .build();
        Flow delFlow3 = Flow.builder()
                //.deviceID("738997584569600")
                .deviceID("738997585356800")
                .flowMatch(delFlowMatch)
                .flowActions(delFlowActions)
                .tableID(200)
                .priority(0)
                .appId("delFlows")
                .build();

        ArrayList<Flow> delFlows = new ArrayList<>();
        delFlows.add(delFlow1);
        delFlows.add(delFlow2);
        delFlows.add(delFlow3);

        controller.flowService.deleteFlows(delFlows);
        */


    }
}
