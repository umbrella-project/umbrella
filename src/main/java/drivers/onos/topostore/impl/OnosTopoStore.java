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

package drivers.onos.topostore.impl;

import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoStore;
import api.topostore.TopoSwitch;
import api.topostore.TopoVertex;
import drivers.onos.OnosUrls;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utility.DefaultRestApiHelper;
import utility.JsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * ONOS topology store api driver.
 */
public class OnosTopoStore extends TopoStore {


    public OnosTopoStore() {

    }


    /**
     * Extract links info and store them in topo storage.
     */
    public void addLinks() {

        BufferedReader bufferedReaderLinks = null;
        HttpClient httpClient = null;


        try {
            httpClient = DefaultRestApiHelper
                    .createHttpClient("onos", "rocks");
            HttpGet getRequest = DefaultRestApiHelper
                    .getRequest(httpClient, OnosUrls.LINKS.getUrl());
            HttpResponse response = DefaultRestApiHelper
                    .getResponse(getRequest, httpClient);

            bufferedReaderLinks = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


        } catch (IOException e) {

            e.printStackTrace();
        }

        JsonBuilder jsonParser = new JsonBuilder();
        JSONObject jsonObjectLinks = jsonParser.createJsonArray(bufferedReaderLinks);

        Iterator<JSONObject> keySetIterator = jsonObjectLinks.keySet().iterator();
        String linksKey = String.valueOf(keySetIterator.next());
        JSONArray linksObject = (JSONArray) jsonObjectLinks.get(linksKey);
        Iterator<JSONObject> links = linksObject.iterator();

        while (links.hasNext()) {
            JSONObject link = (JSONObject) links.next();
            JSONObject srcObject = (JSONObject) link.get("src");
            String srcDeviceName = (String) srcObject.get("device");
            TopoSwitch topoSwitch = new TopoSwitch(srcDeviceName);
            this.addSwitch(topoSwitch);
        }

        links = linksObject.iterator();

        while (links.hasNext()) {

            TopoEdge topoEdge = new TopoEdge();
            JSONObject link = links.next();

            JSONObject srcObject = (JSONObject) link.get("src");
            JSONObject dstObject = (JSONObject) link.get("dst");
            String linkType = (String) link.get("type");
            String linkState = (String) link.get("state");
            String srcDeviceName = (String) srcObject.get("device");
            String dstDeviceName = (String) dstObject.get("device");
            String srcPort = (String) srcObject.get("port");
            String dstPort = (String) dstObject.get("port");


            topoEdge.setSrcPort(srcPort);
            topoEdge.setDstPort(dstPort);
            topoEdge.setSrc(srcDeviceName);
            topoEdge.setDst(dstDeviceName);
            //topoEdge.setType(linkType.toString());
            topoEdge.setState(linkState.toString());
            topoEdge.setWeight(1);
            topoEdge.setLabel(srcDeviceName + ":" + srcPort + "," + dstDeviceName + ":" + dstPort);
            topoEdge.setType(TopoEdgeType.SWITCH_SWITCH);
            this.addEdge(topoEdge);

        }

        DefaultRestApiHelper.httpClientShutDown(httpClient);


    }

    /**
     * Extract hosts info and store them in topo storage.
     */
    public void addHosts() {

        BufferedReader bufferedReaderHosts = null;
        HttpClient httpClient = null;

        try {
            httpClient = DefaultRestApiHelper
                    .createHttpClient("onos", "rocks");
            HttpGet getRequest = DefaultRestApiHelper
                    .getRequest(httpClient, OnosUrls.HOSTS.getUrl());
            HttpResponse response = DefaultRestApiHelper
                    .getResponse(getRequest, httpClient);

            bufferedReaderHosts = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

        } catch (IOException e) {

            e.printStackTrace();
        }

        JsonBuilder jsonParser = new JsonBuilder();
        JSONObject hostsJsonObject = jsonParser.createJsonArray(bufferedReaderHosts);

        Iterator<JSONObject> hostsIterator = hostsJsonObject.keySet().iterator();

        String hostsKey = String.valueOf(hostsIterator.next());
        JSONArray HostsObject = (JSONArray) hostsJsonObject.get(hostsKey);
        Iterator<JSONObject> hosts = HostsObject.iterator();

        while (hosts.hasNext()) {
            TopoHost topoHost = new TopoHost();

            ArrayList<String> ipAddressesList = new ArrayList<>();

            JSONObject host = hosts.next();
            String hostID = (String) host.get("id");
            String hostMAC = (String) host.get("mac");
            JSONArray ipAddresses = (JSONArray) host.get("ipAddresses");
            JSONArray locations = (JSONArray) host.get("locations");
            Iterator<JSONObject> locationIterator = locations.iterator();

            for (Object ipObject : ipAddresses) {
                ipAddressesList.add(ipObject.toString());

            }

            while (locationIterator.hasNext()) {
                JSONObject hostLocation = locationIterator.next();
                String elementId = (String) hostLocation.get("elementId");
                String port = (String) hostLocation.get("port");
                topoHost.getHostLocation().setElementID(elementId);
                topoHost.getHostLocation().setPort(port);
            }

            topoHost.setHostID(hostID);
            topoHost.setHostMac(hostMAC);
            topoHost.setHostIPAddresses(ipAddressesList);
            this.addHost(topoHost);

            Set<TopoVertex> topoVertices = this.getGraph().vertexSet();

            TopoEdge topoEdge = new TopoEdge();
            topoEdge.setSrcPort("0");
            topoEdge.setDstPort(topoHost.getHostLocation().getPort());
            topoEdge.setSrc(topoHost.getHostID());
            topoEdge.setDst(topoHost.getHostLocation().getElementID());
            topoEdge.setWeight(1);
            //topoEdge.setLabel(srcDeviceName + ":" + srcPort + "," + dstDeviceName + ":" + dstPort);
            topoEdge.setType(TopoEdgeType.HOST_SWITCH);
            this.addEdge(topoEdge);

            TopoEdge topoEdgeRev = new TopoEdge();
            topoEdgeRev.setSrcPort(topoHost.getHostLocation().getPort());
            topoEdgeRev.setDstPort("0");
            topoEdgeRev.setSrc(topoHost.getHostLocation().getElementID());
            topoEdgeRev.setDst(topoHost.getHostID());
            topoEdgeRev.setWeight(1);
            //topoEdge.setLabel(srcDeviceName + ":" + srcPort + "," + dstDeviceName + ":" + dstPort);
            topoEdgeRev.setType(TopoEdgeType.SWITCH_HOST);
            this.addEdge(topoEdgeRev);
        }

        DefaultRestApiHelper.httpClientShutDown(httpClient);
    }

    /**
     * Fetch topology information.
     */
    @Override
    public void fetchTopo() {
        addLinks();
        addHosts();
    }

    @Override
    public void updateTopo() {
        this.clear();
        addLinks();
        addHosts();

    }
}
