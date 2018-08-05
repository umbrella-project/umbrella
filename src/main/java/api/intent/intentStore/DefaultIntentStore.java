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

package api.intent.intentStore;

import api.intent.Intent;
import api.intent.IntentId;

import java.util.HashMap;
import java.util.Map;

public class DefaultIntentStore implements IntentStore {


    private static DefaultIntentStore intentStore;
    private Map<IntentId, Intent> currentIntents;


    private DefaultIntentStore() {
        currentIntents = new HashMap<>();
    }


    public static DefaultIntentStore getInstance() {
        if (intentStore == null) {
            intentStore = new DefaultIntentStore();
        }

        return intentStore;

    }

    public Map<IntentId, Intent> getCurrentIntents() {
        return this.currentIntents;
    }


    @Override
    public void addIntent(Intent intent) {

        currentIntents.put(intent.getIntentId(), intent);


    }

    @Override
    public void removeIntent(Intent intent) {

    }


    public Iterable<Intent> getCurrentIntentValues() {
        return currentIntents.values();
    }
}
