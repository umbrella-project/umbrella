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

package drivers.onos.flowservice.impl;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowMatch;
import api.flowservice.FlowService;
import drivers.onos.OnosUrls;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Class implementing ONOS flow service driver.
 */
public class OnosFlowService extends FlowService {

    private static Logger log = Logger.getLogger(OnosFlowService.class);

    private final String APP_ID = "?appId=";

    public OnosFlowService() {
    }

    /**
     * Adds a flow to a network device.
     *
     * @param flow flow object.
     * @return flow id.
     */
    @Override
    public String addFlow(Flow flow) {

        StringEntity stringEntity = null;

        String jsonFinalResult = toJsonString(flow);


        try {
            stringEntity = new StringEntity(jsonFinalResult);
            stringEntity.setContentType("Application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        DefaultRestApiHelper restApiHelper = new DefaultRestApiHelper();
        HttpClient httpClient;

        httpClient = DefaultRestApiHelper.createHttpClient("onos", "rocks");

        BufferedReader br = restApiHelper.httpPostRequest(httpClient,
                OnosUrls.FLOWS.getUrl() + APP_ID + flow.getAppId(), stringEntity);

        String output = null;
        String flowId = null;

        try {
            while ((output = br.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONObject outputObject = null;
                try {
                    outputObject = (JSONObject) parser.parse(output);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                JSONArray flows = (JSONArray) outputObject.get("flows");

                JSONObject flowIdObj = (JSONObject) flows.get(0);
                flowId = flowIdObj.get("flowId").toString();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        restApiHelper.httpClientShutDown(httpClient);

        return flowId;

    }

    @Override
    public String[] addFlows(List<Flow> flows) {
        return new String[flows.size()];
    }

    /**
     * Deletes a flow from a network device.
     *
     * @param deviceID device ID.
     * @param FlowId   flow id.
     * @return boolean.
     */
    @Override
    public boolean deleteFlow(String deviceID, String FlowId) {

        final String APPS = "/application/";
        HttpClient httpClient;

        httpClient = DefaultRestApiHelper.createHttpClient("onos", "rocks");
        DefaultRestApiHelper.httpDelRequest(httpClient,
                OnosUrls.FLOWS.getUrl() + "/" + deviceID + "/" + FlowId);
        return false;
    }

    @Override
    public boolean[] deleteFlows(List<Flow> flows) {
        return new boolean[0];
    }


    /**
     * A json string generator based on a given flow.
     *
     * @param flow flow object.
     * @return a json string.
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
        }



        Integer priority = flow.getPriority();
        if (priority == null) {
            priority = new Integer(100);
        }

        JSONObject jsonFinalResult = new JSONObject();
        JSONObject[] actionObjects = new JSONObject[100];
        JSONObject[] matchObjects = new JSONObject[100];

        StringEntity stringEntity = null;
        for (int i = 0; i < actionObjects.length; i++) {
            actionObjects[i] = new JSONObject();
            matchObjects[i] = new JSONObject();
        }


        int actionCounter = 0;
        int matchCounter = 0;

        JSONArray actionArrays = new JSONArray();
        JSONArray matchArrays = new JSONArray();
        JSONObject flowsObject = new JSONObject();
        JSONArray flowArrays = new JSONArray();


        JSONObject jsonResult = new JSONObject();

        jsonResult.put("priority", priority.toString());
	jsonResult.put("tableId", tableID);
        if (!flow.isPermanent()) {
            jsonResult.put("timeout", flow.getTimeOut());
            jsonResult.put("isPermanent", false);

        } else if (flow.isPermanent()) {

            jsonResult.put("isPermanent", true);
        }

        jsonResult.put("deviceId", flow.getDeviceID());


        JSONObject tempActionObjects = new JSONObject();
        JSONObject tempSelectorObjects = new JSONObject();
        JSONObject tempFlowsObjects = new JSONObject();

        Integer inPort = flowMatch.getIN_PORT();

        if (inPort != null) {
            matchObjects[matchCounter].put("type", "IN_PORT");
            matchObjects[matchCounter].put("port", inPort.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        String ethSrc = flowMatch.getETH_SRC();
        String ethDst = flowMatch.getETH_DST();
        Integer ethType = flowMatch.getETH_TYPE();


        if (ethSrc != null) {
            matchObjects[matchCounter].put("type", "ETH_SRC");
            matchObjects[matchCounter].put("mac", ethSrc.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        if (ethDst != null) {
            matchObjects[matchCounter].put("type", "ETH_DST");
            matchObjects[matchCounter].put("mac", ethDst.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        if (ethType != null) {
            matchObjects[matchCounter].put("type", "ETH_TYPE");
            matchObjects[matchCounter].put("ethType", ethType.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        Integer ipProto = flowMatch.getIP_PROTO();

        if (ipProto != null) {
            matchObjects[matchCounter].put("type", "IP_PROTO");
            matchObjects[matchCounter].put("protocol", ipProto.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        String ipv4Src = flowMatch.getIPV4_SRC();
        String ipv4Dst = flowMatch.getIPv4_DST();

        if (ipv4Src != null) {
            matchObjects[matchCounter].put("type", "IPV4_SRC");
            matchObjects[matchCounter].put("ip", ipv4Src.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        if (ipv4Dst != null) {
            matchObjects[matchCounter].put("type", "IPV4_DST");
            matchObjects[matchCounter].put("ip", ipv4Dst.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        Integer tcpSrcPort = flowMatch.getTCP_SRC();
        Integer tcpDstPort = flowMatch.getTCP_DST();

        if (tcpSrcPort != null) {
            matchObjects[matchCounter].put("type", "TCP_SRC");
            matchObjects[matchCounter].put("tcpPort", tcpSrcPort.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        if (tcpDstPort != null) {
            matchObjects[matchCounter].put("type", "TCP_DST");
            matchObjects[matchCounter].put("tcpPort", tcpDstPort.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        Integer icmpv4Type = flowMatch.getICMPV4_TYPE();
        Integer icmpv4Code = flowMatch.getICMPV4_CODE();

        if (icmpv4Type != null) {

            matchObjects[matchCounter].put("type", "ICMPV4_TYPE");
            matchObjects[matchCounter].put("icmpType", icmpv4Type.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        if (icmpv4Code != null) {
            matchObjects[matchCounter].put("type", "ICMPV4_CODE");
            matchObjects[matchCounter].put("icmpCode", icmpv4Code.toString());
            matchArrays.add(matchObjects[matchCounter]);
            matchCounter++;

        }

        /*actionObjects[actionCounter].put("tableId", tableID);
        actionObjects[actionCounter].put("type", "TABLE");
        actionArrays.add(actionObjects[actionCounter]);
        actionCounter++;*/


        for (FlowAction action : flowActions) {


            switch (action.getActionType()) {
                case OUTPUT:
                    Integer outputPort = (Integer) action.getActionData();
                    actionObjects[actionCounter].put("port", outputPort);
                    actionObjects[actionCounter].put("type", "OUTPUT");
                    actionArrays.add(actionObjects[actionCounter]);
                    actionCounter++;
                    break;

                case CONTROLLER:
                    actionObjects[actionCounter].put("port", "CONTROLLER");
                    actionObjects[actionCounter].put("type", "OUTPUT");
                    actionArrays.add(actionObjects[actionCounter]);
                    actionCounter++;
                    break;


                case DROP:
                    break;
            }
        }

        tempActionObjects.put("instructions", actionArrays);
        tempSelectorObjects.put("criteria", matchArrays);

        jsonResult.put("treatment", tempActionObjects);
        jsonResult.put("selector", tempSelectorObjects);

        flowArrays.add(jsonResult);
        //log.debug(jsonResult.toJSONString() + "\n");

        jsonFinalResult.put("flows", flowArrays);

        String flowString = jsonFinalResult.toJSONString();

        //log.debug(flowString + "\n");

        return flowString;
    }


}
