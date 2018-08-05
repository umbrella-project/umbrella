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

public abstract class Intent {

    public static final int DEFAULT_POLICY_PRIORITY = 100;
    public static final int POLICY_MIN_PRIORITY = 1;
    public static final int POLICY_MAX_PRIORITY = (1 << 16) - 1;
    private final IntentId intentId;
    private int priority;
    private int version;


    protected Intent() {
        intentId = null;
        priority = DEFAULT_POLICY_PRIORITY;
        version = 0;
    }

    public IntentId getIntentId() {
        return intentId;
    }

    public int getPriority() {
        return priority;
    }

    public int getVersion() {
        return version;
    }

    public abstract static class Builder {
        protected IntentId intentId;
        protected int priority;
        protected int version;

        protected Builder() {

        }

        public Builder intentId(IntentId intentId) {
            this.intentId = intentId;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }


    }


}
