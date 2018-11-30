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
 * Represents Flow actions.
 */
public class FlowAction {

    /**
     * Flow action type (e.g DROP, FORWARD, CONTROLLER, ...).
     */
    private FlowActionType actionType;

    /**
     * Flow action data.
     */
    private Object actionData;

    /**
     * Flow action constructor based on action type.
     * @param type flow action type.
     */
    public FlowAction(FlowActionType type) {
        this.actionType = type;
    }

    /** Flow action constructor based on action type and data.
     * @param type flow action type.
     * @param data flow action data.
     */
    public FlowAction(FlowActionType type, int data) {
        this.actionType = type;
        this.actionData = data;
    }

    /**
     * Returns action type.
     *
     * @return action type.
     */
    public FlowActionType getActionType() {
        return this.actionType;
    }

    /**
     * Returns action data.
     *
     * @return action data object.
     */
    public Object getActionData() {
        return this.actionData;
    }
}
