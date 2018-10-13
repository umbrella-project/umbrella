package drivers.ryu.topostore.impl;

import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoStore;
import api.topostore.TopoSwitch;
import drivers.ryu.RyuUrls;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RyuTopoStore extends TopoStore {


    public RyuTopoStore() {


    }

    /**
     * Fetch topology information.
     */

    @Override
    public void fetchTopo() {
        addLinks();
        addHosts();

    }


    /**
     * Extract links info and store them in topo storage.
     */

    public void addLinks() {

        BufferedReader bufferedReaderLinks = null;
        HttpClient httpClient = null;


        try {
            httpClient = DefaultRestApiHelper
                    .createHttpClient(" ", " ");
            HttpGet getRequest = DefaultRestApiHelper
                    .getRequest(httpClient, RyuUrls.LINKS.getUrl());
            HttpResponse response = DefaultRestApiHelper
                    .getResponse(getRequest, httpClient);

            bufferedReaderLinks = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


        } catch (IOException e) {

            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = bufferedReaderLinks.readLine()) != null) {

                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String jsonLinksString = sb.toString();

        org.json.JSONArray jsonArrayLinks = new org.json.JSONArray(jsonLinksString);

        for (int i = 0; i < jsonArrayLinks.length(); i++) {

            String srcDeviceName = jsonArrayLinks.getJSONObject(i).getJSONObject("src").get("dpid").toString();
            TopoSwitch topoSwitch = new TopoSwitch(srcDeviceName);
            this.addSwitch(topoSwitch);

        }


        for (int i = 0; i < jsonArrayLinks.length(); i++) {
            TopoEdge topoEdge = new TopoEdge();


            org.json.JSONObject srcObject = jsonArrayLinks.getJSONObject(i).getJSONObject("src");
            org.json.JSONObject dstObject = jsonArrayLinks.getJSONObject(i).getJSONObject("dst");

            String srcDeviceName = (String) srcObject.get("dpid");
            String dstDeviceName = (String) dstObject.get("dpid");
            String srcPort = (String) srcObject.get("port_no");
            String dstPort = (String) dstObject.get("port_no");

            topoEdge.setSrcPort(srcPort);
            topoEdge.setDstPort(dstPort);
            topoEdge.setSrc(srcDeviceName);
            topoEdge.setDst(dstDeviceName);
            topoEdge.setWeight(1);
            topoEdge.setLabel(srcDeviceName + ":" + srcPort + "," + dstDeviceName + ":" + dstPort);
            topoEdge.setType(TopoEdgeType.SWITCH_SWITCH);
            this.addEdge(topoEdge);


        }

        DefaultRestApiHelper.httpClientShutDown(httpClient);

    }


    public BufferedReader getHostPerDpid(String dpid) {

        BufferedReader bufferedReaderHosts = null;
        HttpClient httpClient = null;


        try {
            httpClient = DefaultRestApiHelper
                    .createHttpClient(" ", " ");
            HttpGet getRequest = DefaultRestApiHelper
                    .getRequest(httpClient, RyuUrls.HOSTS.getUrl() + "/" + dpid);
            HttpResponse response = DefaultRestApiHelper
                    .getResponse(getRequest, httpClient);

            bufferedReaderHosts = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


        } catch (IOException e) {

            e.printStackTrace();
        }

        return bufferedReaderHosts;


    }


    public void addHosts() {

        for (TopoSwitch topoSwitch : this.getSwitches()) {

            BufferedReader bufferedReaderHosts = getHostPerDpid(topoSwitch.getSwitchID());

            StringBuilder sb = new StringBuilder();
            String line;

            try {
                while ((line = bufferedReaderHosts.readLine()) != null) {

                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            org.json.JSONObject hostsList = new org.json.JSONObject();

            String jsonHostsString = sb.toString();

            org.json.JSONArray jsonArrayHosts = new org.json.JSONArray(jsonHostsString);

            for (int i = 0; i < jsonArrayHosts.length(); i++) {

                TopoHost topoHost = new TopoHost();
                ArrayList<String> ipAddressesList = new ArrayList<>();


                org.json.JSONObject portJsonObject = (org.json.JSONObject) jsonArrayHosts.getJSONObject(i).get("port");
                String hostMAC = (String) jsonArrayHosts.getJSONObject(i).get("mac");
                String deviceId = portJsonObject.getString("dpid");
                String attachmentPortNo = portJsonObject.getString("port_no");
                String hostID = portJsonObject.getString("name");


                org.json.JSONArray ipJsonArray = (org.json.JSONArray) jsonArrayHosts.getJSONObject(i).get("ipv4");

                for (Object ipObject : ipJsonArray) {
                    ipAddressesList.add(ipObject.toString());

                }

                topoHost.setHostID(hostID);
                topoHost.setHostMac(hostMAC);
                topoHost.setHostIPAddresses(ipAddressesList);
                topoHost.getHostLocation().setElementID(deviceId);
                topoHost.getHostLocation().setPort(attachmentPortNo);
                this.addHost(topoHost);


            }

        }

    }


    @Override
    public void updateTopo() {
        this.clear();
        addLinks();
        addHosts();

    }
}
