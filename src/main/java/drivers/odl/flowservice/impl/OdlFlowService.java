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

package drivers.odl.flowservice.impl;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowMatch;
import api.flowservice.FlowService;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utility.DefaultRestApiHelper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Class implementing Flow Service in ODL
 */
public class OdlFlowService extends FlowService {

    private static int flowId;
    private static Logger log = Logger.getLogger(OdlFlowService.class);

    /**
     * Default constructor
     */
    public OdlFlowService() {

        Random random = new Random();
        this.flowId = random.nextInt(1000);
    }

    /**
     * Adds a flow in the ODL controller
     *
     * @param flow
     * @return
     */
    @Override
    public String addFlow(Flow flow) {

        if (flow.getDeviceID() == null) {
            return null;
        }

        FlowMatch flowMatch = flow.getFlowMatch();
        if (flowMatch == null) {
            return null;
        }

        List<FlowAction> flowActions = flow.getFlowActions();
        if (flowActions.size() == 0) {
            return null;
        }

        String jsonString = this.toJsonString(flow);

        log.debug("OdlFlowService::addFlow : flowString " + jsonString + "\n");
        int tableID = flow.getTableID();

        String flowID = flow.getFlowID();

        String uri = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + flow.getDeviceID() + "/table/" + tableID
                + "/flow/" + flowID;

        log.debug("OdlFlowService::addFlow : uri " + uri + "\n");

        HttpClient httpClient;

        httpClient = DefaultRestApiHelper.createHttpClient("admin", "admin");

        DefaultHttpRequestFactory defaultHttpRequestFactory = new DefaultHttpRequestFactory();

        HttpPut httpPut = new HttpPut(uri);

        httpPut.addHeader("Content-type", "application/json");

        httpPut.setEntity(new StringEntity(jsonString, "utf-8"));

        try {
            HttpResponse httpResponse = httpClient.execute(httpPut);

            StatusLine statusLine = httpResponse.getStatusLine();

            log.debug("OdlFlowService::addFlow : http Status " + statusLine.getStatusCode()
                    + " " + statusLine.getReasonPhrase() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "table/" + tableID + "/flow/" + flowID;
    }

    public String[] addFlows(List<Flow> flows) {
        String[] status = new String[flows.size()];

        return status;
    }

    /**
     * Delete a flow rule from a given device.
     *
     * @param deviceID device ID.
     * @param flowId   flow ID.
     * @return a boolean
     */
    public boolean deleteFlow(String deviceID, String flowId) {

        String uri = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + deviceID + "/" + flowId;

        log.debug("OdlFlowService.deleteFlow :: uri : " + uri + "\n");

        HttpClient defaultHttpClient;

        defaultHttpClient = DefaultRestApiHelper.createHttpClient("admin", "admin");

        DefaultRestApiHelper.httpDelRequest(defaultHttpClient, uri);

        return true;
    }

    public boolean[] deleteFlows(List<Flow> flows) {
        boolean[] status = new boolean[flows.size()];

        return status;
    }

    /**
     * A json string generator based on a given flow.
     *
     * @param flow flow object
     * @return a json string
     */

    @Override
    public String toJsonString(Flow flow) {

        if (flow.getDeviceID() == null) {
            return null;
        }

        FlowMatch flowMatch = flow.getFlowMatch();
        if (flowMatch == null) {
            return null;
        }

        List<FlowAction> flowActions = flow.getFlowActions();
        if (flowActions.size() == 0) {
            return null;
        }

        Integer tableID = flow.getTableID();
        if (tableID == null) {
            tableID = new Integer(0);
            flow.setTableID(tableID);
        }

        Integer priority = flow.getPriority();
        if (priority == null) {
            priority = new Integer(100);
        }

        int currFlowId = this.flowId++;
        flow.setFlowID(new Integer(currFlowId).toString());

        JSONObject jsonHighestLevelFlow = new JSONObject();

        JSONArray jsonFlowArray = new JSONArray();

        JSONObject jsonFlow = new JSONObject();

        jsonFlow.put("id", new Integer(currFlowId).toString());

        jsonFlow.put("table_id", tableID.toString());
        jsonFlow.put("priority", priority.toString());

        Integer timeOut = flow.getTimeOut();
        if (timeOut != null) {
            jsonFlow.put("idle-timeout", timeOut.toString());
        }

        JSONObject jsonMatch = new JSONObject();

        Integer inPort = flowMatch.getIN_PORT();
        if (inPort != null) {
            jsonMatch.put("in-port", inPort.toString());
        }

        String ethSrc = flowMatch.getETH_SRC();
        String ethDst = flowMatch.getETH_DST();
        Integer ethType = flowMatch.getETH_TYPE();

        if (ethSrc != null || ethDst != null || ethType != null) {

            JSONObject jsonEth = new JSONObject();

            if (ethSrc != null) {
                JSONObject jsonEthSrc = new JSONObject();
                jsonEthSrc.put("address", ethSrc);
                jsonEth.put("ethernet-source", jsonEthSrc);
            }

            if (ethDst != null) {
                JSONObject jsonEthDst = new JSONObject();
                jsonEthDst.put("address", ethDst);
                jsonEth.put("ethernet-destination", jsonEthDst);
            }

            if (ethType != null) {
                JSONObject jsonEthType = new JSONObject();
                jsonEthType.put("type", ethType.intValue());
                jsonEth.put("ethernet-type", jsonEthType);
            }

            jsonMatch.put("ethernet-match", jsonEth);
        }

        Integer ipProto = flowMatch.getIP_PROTO();
        if (ipProto != null) {
            JSONObject jsonIpProto = new JSONObject();
            jsonIpProto.put("ip-protocol", ipProto.intValue());

            jsonMatch.put("ip-match", jsonIpProto);
        }

        String ipv4Src = flowMatch.getIPV4_SRC();
        String ipv4Dst = flowMatch.getIPv4_DST();

        if (ipv4Src != null) {
            jsonMatch.put("ipv4-source", ipv4Src + "/32");
        }

        if (ipv4Dst != null) {
            jsonMatch.put("ipv4-destination", ipv4Dst + "/32");
        }

        Integer tcpSrcPort = flowMatch.getTCP_SRC();
        Integer tcpDstPort = flowMatch.getTCP_DST();

        if (tcpSrcPort != null) {
            jsonMatch.put("tcp-source-port", tcpSrcPort.intValue());
        }

        if (tcpDstPort != null) {
            jsonMatch.put("tcp-destination-port", tcpDstPort.intValue());
        }

        Integer icmpv4Type = flowMatch.getICMPV4_TYPE();
        Integer icmpv4Code = flowMatch.getICMPV4_CODE();

        if (icmpv4Type != null || icmpv4Code != null) {
            JSONObject jsonIcmp = new JSONObject();

            if (icmpv4Type != null) {
                jsonIcmp.put("icmpv4-type", icmpv4Type.intValue());
            }

            if (icmpv4Code != null) {
                jsonIcmp.put("icmpv4-code", icmpv4Code.intValue());
            }

            jsonMatch.put("icmpv4-match", jsonIcmp);
        }

        JSONArray jsonInstructionArray = new JSONArray();

        JSONObject jsonInstruction = new JSONObject();

        jsonInstruction.put("order", 0);

        JSONArray jsonActions = new JSONArray();

        for (FlowAction action : flowActions) {

            JSONObject jsonAction = new JSONObject();

            switch (action.getActionType()) {
                case OUTPUT:
                    Integer outputPort = (Integer) action.getActionData();
                    JSONObject jsonOutput = new JSONObject();
                    jsonOutput.put("output-node-connector", outputPort.toString());
                    jsonAction.put("output-action", jsonOutput);
                    jsonAction.put("order", 0);
                    break;

                case DROP:
                    jsonAction.put("drop-action", new JSONObject());
                    jsonAction.put("order", 0);
                    break;
            }

            jsonActions.add(jsonAction);
        }

        JSONObject jsonTemp = new JSONObject();
        jsonTemp.put("action", jsonActions);

        jsonInstruction.put("apply-actions", jsonTemp);

        jsonInstructionArray.add(jsonInstruction);

        jsonFlow.put("match", jsonMatch);

        jsonTemp = new JSONObject();
        jsonTemp.put("instruction", jsonInstructionArray);
        jsonFlow.put("instructions", jsonTemp);

        jsonFlowArray.add(jsonFlow);

        jsonHighestLevelFlow.put("flow", jsonFlowArray);

        String jsonString = jsonHighestLevelFlow.toJSONString();

        return jsonString;
    }
}