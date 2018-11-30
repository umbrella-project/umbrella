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

import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;

/**
 * Representation of network links in a network topology.
 */
public class TopoEdge extends DefaultEdge implements TopoEdgeInterface {

    /**
     * Source network device.
     */
    private String src;
    /**
     * Destination network device.
     */
    private String dst;
    /**
     * Source device port.
     */
    private String srcPort;
    /**
     * Destination device port.
     */
    private String dstPort;
    /**
     * Topology link state.
     */
    private String state;
    /**
     * Topology link type.
     */
    private TopoEdgeType type;
    /**
     * Topology link weight.
     */
    private int weight;
    /**
     * Topology link label.
     */
    private String label;

    public TopoEdge() {
        src = null;
        dst = null;
        srcPort = null;
        dstPort = null;
        state = null;
        type = null;
        weight = 0;
        label = null;
    }

    /**
     * Returns type of a link.
     *
     * @return type of a link.
     */
    public TopoEdgeType getType() {
        return this.type;
    }

    /**
     * Sets type of a link.
     *
     * @param type TopoEdgeType
     */
    public void setType(TopoEdgeType type) {
        this.type = type;
    }

    /**
     * Returns destination attachment point in a topology link.
     *
     * @return destination attachment point.
     */
    public String getDst() {
        return this.dst;
    }

    /**
     * Sets destination attachment point in a topology link.
     *
     * @param dst destination attachment point.
     */
    public void setDst(String dst) {
        this.dst = dst;
    }


    /**
     * Returns link destination port.
     * @return destination port.
     */
    public String getDstPort() {
        return this.dstPort;
    }

    /**
     * Sets link destination port.
     * @param dstPort destination port.
     */
    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    /**
     * Returns source network device of a link.
     * @return source network device ID.
     */
    public String getSrc() {
        return this.src;
    }

    /**
     * Sets source network device of a link.
     * @param src source network device ID.
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Returns link source port.
     * @return source port.
     */
    public String getSrcPort() {
        return this.srcPort;
    }

    /**
     * Sets link source port.
     * @param srcPort source port.
     */
    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    /**
     * Returns state of a link in the topology.
     * @return state of a link.
     */
    public String getState() {
        return this.state;
    }

    /**
     * Sets state of a link in the topology.
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns weight of a link.
     * @return weight of a link.
     */
    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLabel() {
        return this.label;
    }


    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TopoEdge)) {
            return false;
        }
        TopoEdge topoEdge = (TopoEdge) o;
        return Objects.equals(src, topoEdge.src) && Objects.equals(dst, topoEdge.dst)
                && Objects.equals(srcPort, topoEdge.srcPort) && Objects.equals(dstPort, topoEdge.dstPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, srcPort, dst, dstPort);
    }
}