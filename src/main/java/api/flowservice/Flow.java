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
 * Represents a flow including a list of match fields and  actions.
 */
public class Flow implements FlowInterface {


    /**
     * An object of match fields.
     */
    protected FlowMatch flowMatch;
    /**
     * A List of flow actions.
     */
    protected List<FlowAction> flowActions;

    /**
     * Priority of a flow.
     */
    protected Integer priority;

    /**
     * Flow rule table ID.
     */
    protected Integer tableID;
    /**
     * Flow rule device ID.
     */
    protected String deviceID;

    /**
     * Flow rule time out.
     */
    protected Integer timeOut;
    /**
     *
     */
    protected boolean isPermanent;
    /**
     * Flow rule cookie.
     */
    protected Integer cookie;
    /**
     * An application ID for a flow rule.
     */
    protected String appId;
    /**
     * flow rule ID.
     */
    protected String flowID;

    /**
     * Default constructor
     */
    protected Flow() {
    }

    /**
     * Constructor based on a builder
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


    /**
     * Returns a builder object.
     * @return Builder object.
     */
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
     * Returns the priority.
     *
     * @return priority.
     */
    public Integer getPriority() {
        return this.priority;
    }

    /**
     * Returns the table ID.
     * @return table ID.
     */
    public Integer getTableID() {
        return this.tableID;
    }


    /**
     * Sets table ID.
     * @param tableID flow rule table ID.
     */
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    /**
     * Returns the flow time out.
     *
     * @return time out
     */
    public Integer getTimeOut() {
        return this.timeOut;
    }

    /**
     * Returns the application ID.
     *
     * @return app ID.
     */
    public String getAppId() {
        return this.appId;
    }

    /**
     * Returns the device ID.
     *
     * @return device ID.
     */
    public String getDeviceID() {
        return this.deviceID;
    }

    /**
     * Returns the cookie.
     *
     * @return cookie.
     */

    /**
     * Returns flow rule ID.
     * @return flow rule ID.
     */
    public String getFlowID() {
        return this.flowID;
    }

    /**
     * Sets flow rule ID.
     * @param flowID flow rule ID.
     */
    public void setFlowID(String flowID) {
        this.flowID = flowID;
    }

    /**
     * Returns flow rule cookie.
     * @return flow rule cookie.
     */
    public Integer getCookie() {
        return this.cookie;
    }

    /**
     * Returns the flow match fields object.
     * @return flow match object.
     */
    public FlowMatch getFlowMatch() {
        return this.flowMatch;
    }

    /**
     * Returns the list of flow action objects.
     *
     * @return flow action objects.
     */
    public List<FlowAction> getFlowActions() {
        return this.flowActions;
    }

    /**
     * Adds a flow match object to a flow rule.
     *
     * @param match match object.
     */
    public void addFlowMatch(FlowMatch match) {
        this.flowMatch = match;
    }

    /**
     * Adds a flow action object to a flow rule.
     * @param action flow action object.
     */
    public void addFlowAction(FlowAction action) {
        this.flowActions.add(action);
    }

    /**
     * Adds a list of flow actions object to a flow rule.
     * @param actions a list of flow action objects.
     */
    public void addFlowActions(List<FlowAction> actions) {
        this.flowActions.addAll(actions);
    }

    /**
     * Builder class to generate a Flow object.
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
         * Instantiates an object of type Flow.
         *
         * @return : Flow object.
         */
        public Flow build() {
            return new Flow(this);
        }
    }
}