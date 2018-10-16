package drivers.ryu.flowservice.impl;

import api.flowservice.Flow;
import api.flowservice.FlowAction;
import api.flowservice.FlowMatch;
import api.flowservice.FlowService;
import drivers.onos.OnosUrls;
import drivers.ryu.RyuUrls;
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
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of Ryu Flow service driver.
 */
public class RyuFlowService extends FlowService {

    private static Logger log = Logger.getLogger(RyuFlowService.class);

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

        httpClient = DefaultRestApiHelper.createHttpClient(" ", " ");

        BufferedReader br = restApiHelper.httpPostRequest(httpClient,
                RyuUrls.ADD_FLOWS.getUrl(), stringEntity);


        String flowId = null;

        restApiHelper.httpClientShutDown(httpClient);

        return flowId;


    }

    @Override
    public String[] addFlows(List<Flow> flows) {
        return new String[0];
    }

    public boolean deleteFlow(Flow flow) {
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

        httpClient = DefaultRestApiHelper.createHttpClient(" ", " ");

        BufferedReader br = restApiHelper.httpPostRequest(httpClient,
                RyuUrls.DELETE_MATCHED_FLOWS.getUrl(), stringEntity);

        restApiHelper.httpClientShutDown(httpClient);

        return true;
    }

    @Override
    public boolean deleteFlow(String deviceID, String FlowId) {
	    return false;
    }

    @Override
    public boolean[] deleteFlows(List<Flow> flows) {

        boolean[] results = new boolean[flows.size()];
        int index = 0;

        for(Flow flow:flows)
        {

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

            httpClient = DefaultRestApiHelper.createHttpClient(" ", " ");

            BufferedReader br = restApiHelper.httpPostRequest(httpClient,
                    RyuUrls.DELETE_MATCHED_FLOWS.getUrl(), stringEntity);


            String flowId = null;

            restApiHelper.httpClientShutDown(httpClient);

            results[index] = true;


        }

        return results;
    }

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
        //if (flowActions.size() == 0) {
        //    return null;
        //}

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
        JSONObject matchObject = new JSONObject();

        StringEntity stringEntity = null;
        for (int i = 0; i < actionObjects.length; i++) {
            actionObjects[i] = new JSONObject();

        }


        int actionCounter = 0;
        int matchCounter = 0;

        JSONArray actionArrays = new JSONArray();
        JSONArray matchArrays = new JSONArray();
        JSONObject flowsObject = new JSONObject();
        JSONArray flowArrays = new JSONArray();

        JSONObject jsonResult = new JSONObject();

	jsonResult.put("dpid", flow.getDeviceID());

        if (!flow.isPermanent()) {
            if(flow.getTimeOut() != null) {
                jsonResult.put("idle_timeout", flow.getTimeOut());
            }
        } else if (flow.isPermanent()) {
            jsonResult.put("hard_timeout", flow.getTimeOut());
        }

        jsonResult.put("priority", priority);
        jsonResult.put("table_id", tableID);


        Integer inPort = flowMatch.getIN_PORT();

        if (inPort != null) {
            matchObject.put("in_port", inPort);
        }

        String ethSrc = flowMatch.getETH_SRC();
        String ethDst = flowMatch.getETH_DST();
        Integer ethType = flowMatch.getETH_TYPE();


        if (ethSrc != null) {

            matchObject.put("eth_src", ethSrc);

        }

        if (ethDst != null) {

            matchObject.put("eth_dst", ethDst);


        }

        if (ethType != null) {

            matchObject.put("eth_type", ethType);


        }


        Integer ipProto = flowMatch.getIP_PROTO();

        if (ipProto != null) {

            matchObject.put("ip_proto", ipProto);


        }

        String ipv4Src = flowMatch.getIPV4_SRC();
        String ipv4Dst = flowMatch.getIPv4_DST();

        if (ipv4Src != null) {

            matchObject.put("ipv4_src", ipv4Src);

        }

        if (ipv4Dst != null) {

            matchObject.put("ipv4_dst", ipv4Dst);


        }

        Integer tcpSrcPort = flowMatch.getTCP_SRC();
        Integer tcpDstPort = flowMatch.getTCP_DST();

        if (tcpSrcPort != null) {

            matchObject.put("tcp_src", tcpSrcPort);


        }

        if (tcpDstPort != null) {

            matchObject.put("tcp_dst", tcpDstPort);


        }

        Integer icmpv4Type = flowMatch.getICMPV4_TYPE();
        Integer icmpv4Code = flowMatch.getICMPV4_CODE();

        if (icmpv4Type != null) {


            matchObject.put("icmpv4_type", icmpv4Type);

        }

        if (icmpv4Code != null) {

            matchObject.put("icmpv4_code", icmpv4Code);

        }


        for (FlowAction action : flowActions) {


            switch (action.getActionType()) {
                case OUTPUT:
                    Integer outputPort = (Integer) action.getActionData();
                    actionObjects[actionCounter].put("type", "OUTPUT");
                    actionObjects[actionCounter].put("port", outputPort);

                    actionArrays.add(actionObjects[actionCounter]);
                    actionCounter++;
                    break;

                case CONTROLLER:
                    actionObjects[actionCounter].put("type", "OUTPUT");
                    actionObjects[actionCounter].put("port", "CONTROLLER");

                    actionArrays.add(actionObjects[actionCounter]);
                    actionCounter++;
                    break;


                case DROP:
                    break;

		case GOTO_TABLE:
		    Integer gotoTableId = (Integer) action.getActionData();
		    actionObjects[actionCounter].put("type", "GOTO_TABLE");
		    actionObjects[actionCounter].put("table_id", gotoTableId);

		    actionArrays.add(actionObjects[actionCounter]);
		    actionCounter++;
		    break;
            }
        }

        jsonResult.put("match", matchObject);
        jsonResult.put("actions", actionArrays);



        String flowString = jsonResult.toJSONString();

        log.info(flowString);

        return flowString;
    }
}
