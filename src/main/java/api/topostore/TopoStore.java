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

package api.topostore;


import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.onlab.packet.MacAddress;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Store topology information
 * and implements topology store api functions.
 */
public abstract class TopoStore implements TopoStoreInterface {

    private static Logger log = Logger.getLogger(TopoStore.class);
    protected Set<TopoVertex> topoVertices;
    Graph<TopoVertex, TopoEdge> topoGraph;
    private Set<TopoHost> topoHosts;
    private Set<TopoSwitch> topoSwitches;
    private Set<TopoEdge> topoEdges;

    public TopoStore() {

        topoVertices = new HashSet<>();
        topoHosts = new HashSet<>();
        topoSwitches = new HashSet<>();
        topoEdges = new HashSet<>();
        topoGraph = new DirectedMultigraph<TopoVertex, TopoEdge>
                (new ClassBasedEdgeFactory<TopoVertex, TopoEdge>(TopoEdge.class));
    }


    public void clear() {

        topoVertices.clear();
        topoHosts.clear();
        topoSwitches.clear();
        topoEdges.clear();
        topoGraph = new DirectedMultigraph<TopoVertex, TopoEdge>
                (new ClassBasedEdgeFactory<TopoVertex, TopoEdge>(TopoEdge.class));

    }

    /* Fetches the topology from the REST API */
    public void fetchTopo() {
    }

    /**
     * Add a network device (i.e. a switch) to the topo store.
     *
     * @param topoSwitch a network switch
     */
    public void addSwitch(TopoSwitch topoSwitch) {
        this.topoSwitches.add(topoSwitch);
        this.topoGraph.addVertex(topoSwitch);
    }

    /**
     * Add an edge to list of topology links and topology graph.
     *
     * @param topoEdge
     */
    public void addEdge(TopoEdge topoEdge) {
        this.topoEdges.add(topoEdge);
        TopoEdgeType edgeType = topoEdge.getType();

        TopoVertex srcVertex = null, dstVertex = null;
        switch (topoEdge.getType()) {
            case HOST_SWITCH:
                srcVertex = new TopoVertex(TopoVertexType.HOST, topoEdge.getSrc());
                dstVertex = new TopoVertex(TopoVertexType.SWITCH, topoEdge.getDst());
                break;

            case SWITCH_HOST:
                srcVertex = new TopoVertex(TopoVertexType.SWITCH, topoEdge.getSrc());
                dstVertex = new TopoVertex(TopoVertexType.HOST, topoEdge.getDst());

            case SWITCH_SWITCH:
                srcVertex = new TopoVertex(TopoVertexType.SWITCH, topoEdge.getSrc());
                dstVertex = new TopoVertex(TopoVertexType.SWITCH, topoEdge.getDst());
        }

        this.topoGraph.addEdge(srcVertex, dstVertex, topoEdge);
    }

    public void addHost(TopoHost topoHost) {
        this.topoHosts.add(topoHost);
        this.topoGraph.addVertex(topoHost);
    }

    /**
     * Return list of network switches in the network topology.
     *
     * @return list of switches.
     */
    public Set<TopoSwitch> getSwitches() {
        return this.topoSwitches;
    }

    /**
     * Return list of hosts in the network topology.
     *
     * @return list of hosts.
     */
    public Set<TopoHost> getHosts() {
        return this.topoHosts;
    }


    /**
     * Find a host based on a given Mac address.
     * @param macAddress macAddress of given host.
     * @return a TopoHost.
     */
    public TopoHost getTopoHost(MacAddress macAddress) {

        for (TopoHost topoHost : topoHosts) {

            if (topoHost.getHostMac().equals(macAddress.toString())) {
                return topoHost;

            }

        }

        return null;

    }

    /**
     * Return topology graph.
     *
     * @return topology graph.
     */
    public Graph<TopoVertex, TopoEdge> getGraph() {
        return this.topoGraph;
    }

    public Set<TopoEdge> getTopoEdges() {
        return this.topoEdges;
    }

    /**
     * Return shortest path between two end-points.
     *
     * @param srcDevice source end point.
     * @param dstDevice destination end point.
     * @return a shortest path.
     */
    public List<TopoEdge> getShortestPath(String srcDevice, String dstDevice) {
        DijkstraShortestPath<TopoVertex, TopoEdge> dijkstraAlg =
                new DijkstraShortestPath<>(this.topoGraph);

        TopoSwitch src = new TopoSwitch(srcDevice);
        TopoSwitch dst = new TopoSwitch(dstDevice);
        ShortestPathAlgorithm.SingleSourcePaths<TopoVertex, TopoEdge> iPaths = dijkstraAlg.getPaths(src);
        List<TopoEdge> pathEdges = iPaths.getPath(dst).getEdgeList();

        return pathEdges;
    }

    /**
     * Print an end-to-end path between two end points.
     *
     * @param path a path.
     */
    public void printPath(List<TopoEdge> path) {
        log.debug("PATH:\n");
        for (TopoEdge edge : path) {
            log.debug(edge.getSrc() + "," + edge.getSrcPort() + "-->" + edge.getDst() + "," + edge.getDstPort() + " ");
        }
        log.debug("\n");
    }

    /**
     * Print topology information including list of devices, hosts, and links.
     */
    public void printTopo() {
        log.info("List of Devices :\n");
        for (TopoSwitch topoSwitch : this.topoSwitches) {
            log.info("\t" + topoSwitch.getSwitchID() + "\n");
        }

        log.info("List of Edges :\n");
        for (TopoEdge topoEdge : this.topoEdges) {
            log.info("\t" + topoEdge.getSrc() + "," + topoEdge.getSrcPort() +
                    " -> " +
                    topoEdge.getDst() + "," + topoEdge.getDstPort() + "\n");
        }

        log.info("List of Hosts :\n");
        for (TopoHost topoHost : this.topoHosts) {
            log.info("\t" + topoHost.getHostID() + "\n");
            log.info("\t\tMac : " + topoHost.getHostMac() + "\n");
            log.info("\t\tIP : " + topoHost.getHostIPAddresses().get(0) + "\n");
            log.info("\t\tAttached to : " + topoHost.getHostLocation().getElementID()
                    + ", port : " + topoHost.getHostLocation().getPort() + "\n");
        }
    }

    public void printLinks() {

        log.info("List of Edges :\n");
        for (TopoEdge topoEdge : this.topoEdges) {
            log.info("\t" + topoEdge.getSrc() + "," + topoEdge.getSrcPort() +
                    " -> " +
                    topoEdge.getDst() + "," + topoEdge.getDstPort() + "\n");
        }

    }

    public void printHosts() {

        log.info("List of Hosts :\n");
        for (TopoHost topoHost : this.topoHosts) {
            log.info("\t" + topoHost.getHostID() + "\n");
            log.info("\t\tMac : " + topoHost.getHostMac() + "\n");
            log.info("\t\tIP : " + topoHost.getHostIPAddresses().get(0) + "\n");
            log.info("\t\tAttached to : " + topoHost.getHostLocation().getElementID() +
                    ", port : " + topoHost.getHostLocation().getPort() + "\n");
        }

    }

    public void printDevices() {
        log.info("List of Devices :\n");
        for (TopoSwitch topoSwitch : this.topoSwitches) {
            log.info("\t" + topoSwitch.getSwitchID() + "\n");
        }

    }
}
