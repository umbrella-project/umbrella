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

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import api.topostore.TopoSwitch;
import drivers.ryu.RyuController;
import org.apache.log4j.Logger;
import org.projectfloodlight.openflow.types.EthType;

import java.util.ArrayList;
import java.util.Set;

import static java.sql.Types.NULL;


public class hpDriver {

    private static Logger log = Logger.getLogger(hpDriver.class);
    private static int TABLE_200 = 200;
    private static int TABLE_100 = 100;
    private static int TABLE_0 = 0;

    private static int TABLE_ID_CTRL_PACKETS = 200;


    public static void main(String[] args) {


        RyuController controller = new RyuController();


        /**
         * This part is to delete all previous flows in all of the tables (0, 100, 200)
         */
        FlowMatch delFlowMatch = FlowMatch.builder().build();

        ArrayList<FlowAction> delFlowActions = new ArrayList<>();

        Set<TopoSwitch> topoSwitchSet = controller.topoStore.getSwitches();
        

        for (TopoSwitch topoSwitch : topoSwitchSet) {

            String deviceId = String.valueOf(Long.parseLong(topoSwitch.getSwitchID(), 16));
            System.out.println(deviceId);
            Flow delFlow1 = Flow.builder()
                    .deviceID(deviceId)
                    .flowMatch(delFlowMatch)
                    .flowActions(delFlowActions)
                    .tableID(TABLE_0)
                    .priority(0)
                    .appId("delFlows")
                    .build();

            Flow delFlow100 = Flow.builder()
                    .deviceID(deviceId)
                    .flowMatch(delFlowMatch)
                    .flowActions(delFlowActions)
                    .tableID(TABLE_100)
                    .priority(0)
                    .appId("delFlows")
                    .build();

            Flow delFlow200 = Flow.builder()
                    .deviceID(deviceId)
                    .flowMatch(delFlowMatch)
                    .flowActions(delFlowActions)
                    .tableID(TABLE_200)
                    .priority(0)
                    .appId("delFlows")
                    .build();


            ArrayList<Flow> delFlows = new ArrayList<>();
            delFlows.add(delFlow1);
            delFlows.add(delFlow100);
            delFlows.add(delFlow200);

            controller.flowService.deleteFlows(delFlows);



        }

        for(TopoSwitch topoSwitch:topoSwitchSet)
        {

            String deviceId = String.valueOf(Long.parseLong(topoSwitch.getSwitchID(), 16));
            FlowMatch flowMatch1 = FlowMatch.builder().build();

            FlowAction flowAction1 = new FlowAction(FlowActionType.GOTO_TABLE, 100);

            ArrayList<FlowAction> flowActions = new ArrayList<>();
            flowActions.add(flowAction1);


            Flow defaultTransitionRule100 = Flow.builder()
                    .deviceID(deviceId)
                    .tableID(TABLE_0)
                    .flowMatch(flowMatch1)
                    .flowActions(flowActions)
                    .appId("Tran")
                    .priority(0)
                    .isPermanent(true)
		    .timeOut(0)
                    .build();

            controller.flowService.addFlow(defaultTransitionRule100);



            // default Flow 2 (transition from table 100 to table 200)
            FlowMatch flowMatch2 = FlowMatch.builder().build();
            FlowAction flowAction2 = new FlowAction(FlowActionType.GOTO_TABLE, 200);
            ArrayList<FlowAction> flowActions2 = new ArrayList<>();
            flowActions2.add(flowAction2);


            Flow defaultTransitionRule200 = Flow.builder()
                    .deviceID(deviceId)
                    .tableID(TABLE_100)
                    .flowMatch(flowMatch2)
                    .flowActions(flowActions2)
                    .appId("testApp")
                    .priority(0)
                    .isPermanent(true)
		    .timeOut(0)
                    .build();

            controller.flowService.addFlow(defaultTransitionRule200);


            /**
             * Default Flow rule for handling ARP packets
             */

            FlowMatch arpMatch = FlowMatch.builder().ethType(EthType.ARP.getValue()).build();
            FlowAction arpAction = new FlowAction(FlowActionType.CONTROLLER, NULL);

            ArrayList<FlowAction> arpActionList = new ArrayList<>();
            arpActionList.add(arpAction);

            Flow arpDefaultFlow = Flow.builder()
                    .deviceID(deviceId)
                    .tableID(TABLE_ID_CTRL_PACKETS)
                    .flowMatch(arpMatch)
                    .flowActions(arpActionList)
                    .appId("arpDefaulttRule")
                    .priority(45000)
                    .isPermanent(true)
		    .timeOut(0)
                    .build();


            controller.flowService.addFlow(arpDefaultFlow);


            /**
             * Default Flow rule for handling LLDP packets
             */
            FlowMatch lldpMatch = FlowMatch.builder().ethType(EthType.LLDP.getValue()).build();
            FlowAction lldpAction = new FlowAction(FlowActionType.CONTROLLER, NULL);

            ArrayList<FlowAction> lldpActionList = new ArrayList<>();
            lldpActionList.add(lldpAction);

            Flow lldpDefaultFlow = Flow.builder()
                    .deviceID(deviceId)
                    .tableID(TABLE_ID_CTRL_PACKETS)
                    .flowMatch(lldpMatch)
                    .flowActions(lldpActionList)
                    .appId("lldpDefaultRule")
                    .priority(45000)
                    .isPermanent(true)
		    .timeOut(0)
                    .build();

            controller.flowService.addFlow(lldpDefaultFlow);




        }










    }
}
