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


        FlowMatch flowMatch = FlowMatch.builder()
                .ethSrc("00:00:00:00:00:00")
                .ethDst("88:11:11:11:11:11")
                .inPort(5)
                .ethType(2048)
                .build();

        FlowAction flowAction = new FlowAction(FlowActionType.OUTPUT, 2);

        ArrayList<FlowAction> flowActions = new ArrayList<>();
        flowActions.add(flowAction);


        Flow flow = Flow.builder()
                .deviceID("0000000000000001")
                .tableID(0)
                .flowMatch(flowMatch)
                .flowActions(flowActions)
                .timeOut(20)
                .appId("testApp")
                .priority(1000)
                .cookie(1)
                .isPermanent(false)
                .build();

        ArrayList<Flow> flowArrayList = new ArrayList<>();
        ryuController.flowService.addFlow(flow);
        flowArrayList.add(flow);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //ryuController.flowService.deleteFlow(flow.getDeviceID(), flowId);

        //ryuController.flowService.deleteFlows(flowArrayList);


    }
}
