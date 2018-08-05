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

import api.flowservice.Flow;

public abstract class FlowIntent extends Intent {

    private final Flow flow;


    public FlowIntent() {
        super();
        this.flow = null;


    }

    public FlowIntent(Flow flow) {
        super();
        this.flow = flow;
    }

    public Flow getFlow() {
        return this.flow;
    }

    public abstract static class Builder extends Intent.Builder {

        protected Flow flow;


        protected Builder() {

        }


        public Builder intentId(IntentId intentId) {
            return (Builder) super.intentId(intentId);
        }

        public Builder priority(int priority) {
            return (Builder) super.priority(priority);
        }

        public Builder version(int version) {
            return (Builder) super.version(version);
        }


        public Builder flow(Flow flow) {
            this.flow = flow;
            return this;
        }


    }
}
