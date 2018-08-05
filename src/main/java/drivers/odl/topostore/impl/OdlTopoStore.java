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

package drivers.odl.topostore.impl;

import api.topostore.HostLocation;
import api.topostore.TopoEdge;
import api.topostore.TopoEdgeType;
import api.topostore.TopoHost;
import api.topostore.TopoStore;
import api.topostore.TopoSwitch;
import drivers.odl.OdlUrls;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OdlTopoStore extends TopoStore {


    @Override
    public void fetchTopo() {

        BufferedReader br = null;

        try {
            HttpClient httpClient = DefaultRestApiHelper.createHttpClient("admin", "admin");

            HttpGet getRequest = DefaultRestApiHelper.getRequest(httpClient, OdlUrls.TOPOLOGY.getUrl());

            getRequest.addHeader("Accept", "application/json");

            HttpResponse response = DefaultRestApiHelper.getResponse(getRequest, httpClient);

            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject topLevelObj = (JSONObject) jsonParser.parse(br);

            JSONObject networkTopology = (JSONObject) topLevelObj.get("network-topology");

            JSONArray topologies = (JSONArray) networkTopology.get("topology");

            JSONArray nodes = (JSONArray) ((JSONObject) topologies.get(0)).get("node");

            JSONArray links = (JSONArray) (JSONArray) ((JSONObject) topologies.get(0)).get("link");

            for (Object node : nodes) {
                String nodeId = (String) ((JSONObject) node).get("node-id");

                if (nodeId.startsWith("host")) {
                    JSONArray hostAddrs = (JSONArray) ((JSONObject) node).get("host-tracker-service:addresses");
                    String hostMac = (String) ((JSONObject) hostAddrs.get(0)).get("mac");
                    String hostIp = (String) ((JSONObject) hostAddrs.get(0)).get("ip");

                    TopoHost topoHost = new TopoHost();

                    topoHost.setHostID(nodeId);
                    topoHost.setHostMac(hostMac);

                    ArrayList<String> ipAddresses = new ArrayList<String>();
                    ipAddresses.add(hostIp);

                    topoHost.setHostIPAddresses(ipAddresses);

                    JSONArray attachPoints = (JSONArray) ((JSONObject) node).get("host-tracker-service:attachment-points");

                    String attachPoint = (String) ((JSONObject) attachPoints.get(0)).get("tp-id");

                    String[] breakDown = attachPoint.split(":");

                    String deviceId = breakDown[0] + ":" + breakDown[1];
                    String devicePort = breakDown[2];

                    HostLocation hostLocation = new HostLocation();
                    hostLocation.setElementID(deviceId);
                    hostLocation.setPort(devicePort);

                    topoHost.setHostLocation(hostLocation);

                    this.addHost(topoHost);
                } else {
                    TopoSwitch topoSwitch = new TopoSwitch(nodeId);

                    this.addSwitch(topoSwitch);
                }
            }

            for (Object link : links) {

                JSONObject jsonLink = (JSONObject) link;

                String linkId = (String) jsonLink.get("link-id");

                JSONObject sourceObj = (JSONObject) jsonLink.get("source");
                String sourceId = (String) sourceObj.get("source-node");
                String[] fullSourcePortStr = ((String) sourceObj.get("source-tp")).split(":");
                String sourcePort = fullSourcePortStr[fullSourcePortStr.length - 1];

                JSONObject destinationObj = (JSONObject) jsonLink.get("destination");
                String destId = (String) destinationObj.get("dest-node");
                String[] fullDestPortStr = ((String) destinationObj.get("dest-tp")).split(":");
                String destPort = fullDestPortStr[fullDestPortStr.length - 1];

                if (linkId.contains("host")) {
                    continue;
                }

                TopoEdge topoEdge = new TopoEdge();

                topoEdge.setLabel(linkId);
                topoEdge.setSrc(sourceId);
                topoEdge.setSrcPort(sourcePort);
                topoEdge.setDst(destId);
                topoEdge.setDstPort(destPort);
                topoEdge.setType(TopoEdgeType.SWITCH_SWITCH);

                this.addEdge(topoEdge);
            }

            for (TopoHost topoHost : this.getHosts()) {
                TopoEdge topoEdge = new TopoEdge();
                topoEdge.setType(TopoEdgeType.HOST_SWITCH);
                topoEdge.setSrc(topoHost.getHostID());
                topoEdge.setDst(topoHost.getHostLocation().getElementID());
                topoEdge.setSrcPort("0");
                topoEdge.setDstPort(topoHost.getHostLocation().getPort());
                this.addEdge(topoEdge);

                TopoEdge topoEdgeRev = new TopoEdge();
                topoEdgeRev.setType(TopoEdgeType.SWITCH_HOST);
                topoEdgeRev.setSrc(topoHost.getHostLocation().getElementID());
                topoEdgeRev.setDst(topoHost.getHostID());
                topoEdgeRev.setSrcPort(topoHost.getHostLocation().getPort());
                topoEdgeRev.setDstPort("0");
                this.addEdge(topoEdgeRev);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTopo() {
        this.clear();
        fetchTopo();

    }
}
