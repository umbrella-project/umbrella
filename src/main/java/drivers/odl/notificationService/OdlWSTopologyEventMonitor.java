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

package drivers.odl.notificationService;

import api.notificationservice.EventListener;
import api.notificationservice.EventMonitor;
import api.topostore.TopoEdge;
import api.topostore.TopoVertex;
import drivers.controller.Controller;
import drivers.controller.notificationservice.TopologyEvent;
import drivers.controller.notificationservice.TopologyEventType;
import drivers.odl.OdlUrls;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import utility.DefaultRestApiHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class OdlWSTopologyEventMonitor extends EventMonitor {
    private static Logger log = Logger.getLogger(OdlWSTopologyEventMonitor.class);

    public OdlWSTopologyEventMonitor(Controller controller) {
        super(controller);
        this.eventThread();
    }

    private String eventSubscription() {
        BufferedReader bufferedReaderLinks = null;
        HttpClient httpClient = null;

        httpClient = DefaultRestApiHelper
                .createHttpClient("admin", "admin");

        StringEntity xmlEntity = null;
        try {
            xmlEntity = new StringEntity(OdlUrls.EVENT_SUBSCRIPTION_DATA.getUrl());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        BufferedReader br = DefaultRestApiHelper.httpXmlPostRequest(httpClient,
                OdlUrls.EVENT_SUBSCRIPTION.getUrl(), xmlEntity);


        ArrayList<String> outputs = new ArrayList<>();
        String output = null;

        try {
            while ((output = br.readLine()) != null) {
                outputs.add(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputs.get(0);
    }

    private String streamSubscription(String streamName) {
        BufferedReader br = null;
        HttpClient httpClient = null;
        String webSocketAddress = null;


        try {
            httpClient = DefaultRestApiHelper
                    .createHttpClient("admin", "admin");
            HttpGet getRequest = new HttpGet(OdlUrls.STREAM_SUBSCRPTION.getUrl());
            getRequest.addHeader("accept", "application/xml");
            HttpResponse response = DefaultRestApiHelper
                    .getResponse(getRequest, httpClient);

            br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


            webSocketAddress = br.readLine().toString();
            //log.info(webSocketAddress);

            webSocketAddress = webSocketAddress.split("<location xmlns=\"subscribe:to:notification\">")[1];
            webSocketAddress = webSocketAddress.split("</location")[0];
            //log.info(webSocketAddress);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return webSocketAddress;
    }

    public void eventTriggered() {
        Graph<TopoVertex, TopoEdge> currTopo = controller.topoStore.getGraph();

        controller.topoStore.updateTopo();

        for (TopoVertex vertex : currTopo.vertexSet()) {
            if (!controller.topoStore.getGraph().containsVertex(vertex)) {
                for (EventListener eventListener : eventListeners) {
                    eventListener.onEvent(new TopologyEvent(TopologyEventType.NODE_DOWN, vertex));
                }
            }
        }

        for (TopoVertex vertex : controller.topoStore.getGraph().vertexSet()) {
            if (!currTopo.containsVertex(vertex)) {
                for (EventListener eventListener : eventListeners) {
                    eventListener.onEvent(new TopologyEvent(TopologyEventType.NODE_UP, vertex));
                }
            }
        }

        for (TopoEdge edge : currTopo.edgeSet()) {
            if (!controller.topoStore.getGraph().containsEdge(edge)) {
                for (EventListener eventListener : eventListeners) {
                    eventListener.onEvent(new TopologyEvent(TopologyEventType.LINK_DOWN, edge));
                }
            }
        }

        for (TopoEdge edge : controller.topoStore.getGraph().edgeSet()) {
            if (!currTopo.containsEdge(edge)) {
                for (EventListener eventListener : eventListeners) {
                    eventListener.onEvent(new TopologyEvent(TopologyEventType.LINK_UP, edge));
                }
            }
        }
    }

    public void eventThread() {
        String streamName = eventSubscription();
        String webSocketAddress = streamSubscription(streamName);
        System.out.println("Websocket address " + webSocketAddress);
        OdlWSTopologyEventMonitor odlWSTopologyEventMonitor = this;

        new Thread(new Runnable() {
            public void run() {

                org.java_websocket.client.WebSocketClient client = null;
                try {
                    client = new wsClient(new URI(webSocketAddress), odlWSTopologyEventMonitor);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                client.connect();
            }
        }).start();
    }
}