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

package api.intent;

import api.intent.intentStore.DefaultIntentStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IntentId extends IdentifierAbstract implements Identifier {

    private static AtomicInteger uniqueId = new AtomicInteger();
    private int IntentId;

    public IntentId() {
        IntentId = -1;
    }


    @Override
    public int getId() {
        return IntentId;
    }

    public void newId() {

        DefaultIntentStore intentStore = DefaultIntentStore.getInstance();
        if (intentStore.getCurrentIntents().size() >= 0) {
            Set<IntentId> IntentIdSet = intentStore.getCurrentIntents().keySet();

            List<Integer> IntentIdSetValues = new ArrayList<>();
            for (IntentId intentId : IntentIdSet) {
                IntentIdSetValues.add(intentId.getId());
            }
            Collections.sort(IntentIdSetValues);
            int missingNumber = findFirstMissing(IntentIdSetValues, 0, IntentIdSetValues.size() - 1);

            if (missingNumber == IntentIdSetValues.size() + 1) {
                this.IntentId = uniqueId.getAndIncrement();
            } else {
                this.IntentId = missingNumber;
            }
        }

    }
}
