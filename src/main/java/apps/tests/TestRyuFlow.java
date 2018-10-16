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

package apps.tests;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowActionType;
import api.flowservice.FlowMatch;
import drivers.onos.OnosController;
import drivers.ryu.RyuController;

import java.util.ArrayList;

public class TestRyuFlow {

    public static void main(String[] args) {

        RyuController ryuController = new RyuController();

	/*-------------------------------------------*/
	/* This part is to delete all previous flows */
	FlowMatch delFlowMatch = FlowMatch.builder().build();

	ArrayList<FlowAction> delFlowActions = new ArrayList<>();

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

	ryuController.flowService.deleteFlows(delFlows);

	/*-------------------------------------------*/

	/*-------------------------------------------*/
	/* Now, add the basic flows needed for HP switches */
        /*FlowMatch flowMatch = FlowMatch.builder().build();

        FlowAction flowAction = new FlowAction(FlowActionType.GOTO_TABLE, 100);

        ArrayList<FlowAction> flowActions = new ArrayList<>();
        flowActions.add(flowAction);


        Flow flow = Flow.builder()
                .deviceID("738997584569600")
                .tableID(0)
                .flowMatch(flowMatch)
                .flowActions(flowActions)
                .appId("testApp")
                .priority(0)
                //.cookie(1)
                .isPermanent(false)
                .build();

        ArrayList<Flow> flowArrayList = new ArrayList<>();
        ryuController.flowService.addFlow(flow);
        flowArrayList.add(flow);*/
        //try {
        //    Thread.sleep(20000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //ryuController.flowService.deleteFlow(flow.getDeviceID(), flowId);

        //ryuController.flowService.deleteFlows(flowArrayList);


    }
}