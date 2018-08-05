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

import java.util.List;

/**
 * Represents a flow, including match fields and a list of actions
 */
public class Flow implements FlowInterface {

    protected FlowMatch flowMatch;
    protected List<FlowAction> flowActions;
    protected Integer priority;
    protected Integer tableID;
    protected String deviceID;
    protected Integer timeOut;
    protected boolean isPermanent;
    protected Integer cookie;
    protected String appId;
    protected String flowID;

    /**
     * Default constructor
     */
    protected Flow() {
    }

    /**
     * Constructor based on builder
     *
     * @param builder : Builder object to initialize the flow
     */
    protected Flow(Builder builder) {
        this.deviceID = builder.deviceID;
        this.flowMatch = builder.flowMatch;
        this.flowActions = builder.flowActions;
        this.priority = builder.priority;
        this.tableID = builder.tableID;
        this.timeOut = builder.timeOut;
        this.isPermanent = builder.isPermanent;
        this.cookie = builder.cookie;
        this.appId = builder.appId;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Determine a flow is permenant or not.
     *
     * @return a boolean
     */
    public boolean isPermanent() {
        return this.isPermanent;
    }

    /**
     * Return priority.
     *
     * @return priority.
     */
    public Integer getPriority() {
        return this.priority;
    }

    /**
     * Return table ID.
     *
     * @return table ID.
     */
    public Integer getTableID() {
        return this.tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    /**
     * Return flow time out.
     *
     * @return time out
     */
    public Integer getTimeOut() {
        return this.timeOut;
    }

    /**
     * Return application ID.
     *
     * @return app ID.
     */
    public String getAppId() {
        return this.appId;
    }

    /**
     * Return device ID.
     *
     * @return device ID.
     */
    public String getDeviceID() {
        return this.deviceID;
    }

    /**
     * Return cookie.
     *
     * @return cookie.
     */


    public String getFlowID() {
        return this.flowID;
    }

    public void setFlowID(String flowID) {
        this.flowID = flowID;
    }

    public Integer getCookie() {
        return this.cookie;
    }

    /**
     * Return flow match fields object.
     *
     * @return flow match object.
     */
    public FlowMatch getFlowMatch() {
        return this.flowMatch;
    }

    /**
     * Return list of flow action objects.
     *
     * @return flow action objects.
     */
    public List<FlowAction> getFlowActions() {
        return this.flowActions;
    }

    /**
     * Add a flow match object to a flow rule.
     *
     * @param match match object.
     */
    public void addFlowMatch(FlowMatch match) {
        this.flowMatch = match;
    }

    /**
     * Add a flow action object to a flow rule.
     *
     * @param action flow action object.
     */
    public void addFlowAction(FlowAction action) {
        this.flowActions.add(action);
    }

    /**
     * Add a list of flow actions object to a flow rule.
     *
     * @param actions a list of flow action objects.
     */
    public void addFlowActions(List<FlowAction> actions) {
        this.flowActions.addAll(actions);
    }

    /**
     * Builder class to generate a Flow object
     */
    public static class Builder {

        private FlowMatch flowMatch;
        private List<FlowAction> flowActions;
        private Integer priority;
        private Integer tableID;
        private String deviceID;
        private Integer timeOut;
        private boolean isPermanent;
        private Integer cookie;
        private String appId;

        public Builder() {
        }

        public Builder flowMatch(FlowMatch match) {
            this.flowMatch = match;
            return this;

        }

        public Builder priority(int priority) {
            this.priority = new Integer(priority);
            return this;
        }

        public Builder tableID(int tableID) {
            this.tableID = new Integer(tableID);
            return this;
        }

        public Builder deviceID(String deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public Builder timeOut(int timeOut) {
            this.timeOut = new Integer(timeOut);
            return this;
        }

        public Builder isPermanent(boolean isPermanent) {
            this.isPermanent = isPermanent;
            return this;
        }

        public Builder flowActions(List<FlowAction> actions) {
            this.flowActions = actions;
            return this;
        }

        public Builder cookie(int cookie) {
            this.cookie = new Integer(cookie);
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * Instantiates an object of type Flow
         *
         * @return : Flow object
         */
        public Flow build() {
            return new Flow(this);
        }
    }
}