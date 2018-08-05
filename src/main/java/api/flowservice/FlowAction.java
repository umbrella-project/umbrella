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

package api.flowservice;

/**
 * Class representing Flow actions.
 */
public class FlowAction {

    private FlowActionType actionType;
    private Object actionData;

    public FlowAction(FlowActionType type) {
        this.actionType = type;
    }

    public FlowAction(FlowActionType type, int data) {
        this.actionType = type;
        this.actionData = data;
    }

    /**
     * Return action type.
     *
     * @return action type.
     */
    public FlowActionType getActionType() {
        return this.actionType;
    }

    /**
     * Return action data.
     *
     * @return action data object.
     */
    public Object getActionData() {
        return this.actionData;
    }
}
