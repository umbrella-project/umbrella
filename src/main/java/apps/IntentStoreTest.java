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
import api.flowservice.FlowMatch;
import api.intent.PolicyBasedIntent;
import api.intent.compiler.PolicyIntentCompiler;
import api.intent.intentStore.DefaultIntentStore;
import api.intent.networkOperation.IntraRoute;
import org.apache.log4j.Logger;

public class IntentStoreTest {

    private static Logger log = Logger.getLogger(IntentStoreTest.class);

    public static void main(String[] args) {


        DefaultIntentStore intentStore = DefaultIntentStore.getInstance();

        FlowMatch matchFields = FlowMatch.builder().tcpDst(80).build();

        Flow flow = Flow.builder().flowMatch(matchFields).build();

        IntraRoute intraRoute = new IntraRoute();


        PolicyBasedIntent intent = PolicyBasedIntent.builder()
                .networkOpetation(intraRoute)
                .flow(flow)
                .build();

        intentStore.addIntent(intent);

        PolicyIntentCompiler policyIntentCompiler = new PolicyIntentCompiler();
        policyIntentCompiler.compile(intent, null);


        log.info(intent.getFlow().getFlowMatch().getTCP_DST());


    }
}
