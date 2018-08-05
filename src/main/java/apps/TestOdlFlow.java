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
import drivers.odl.OdlController;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class TestOdlFlow {
    private static Logger log = Logger.getLogger(TestOdlFlow.class);

    public static void main(String[] args) {


        OdlController odlController = new OdlController();

        FlowMatch flowMatch = FlowMatch.builder()
                .ethSrc("00:00:00:00:00:00")
                .ethDst("88:11:11:11:11:11")
                .ethType(2048)
                .build();

        FlowAction flowAction = new FlowAction(FlowActionType.OUTPUT, 2);

        ArrayList<FlowAction> flowActions = new ArrayList<>();
        flowActions.add(flowAction);

        Flow flow = Flow.builder()
                .deviceID("openflow:1")
                .tableID(0)
                .flowMatch(flowMatch)
                .flowActions(flowActions)
                .build();

        String flowId = odlController.flowService.addFlow(flow);

        log.debug("Flow installation status : " + flowId + "\n");

        odlController.flowService.deleteFlow("openflow:1", flowId);
    }
}
