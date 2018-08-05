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
    private String src;
    private String dst;
    private String srcPort;
    private String dstPort;
    private String state;
    private TopoEdgeType type;
    private int weight;
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
     * Return type of a link.
     *
     * @return
     */
    public TopoEdgeType getType() {
        return this.type;
    }

    /**
     * Set type of a link.
     *
     * @param type TopoEdgeType
     */
    public void setType(TopoEdgeType type) {
        this.type = type;
    }

    /**
     * Return destination attachment point in a topo edge.
     *
     * @return destination attacment point.
     */
    public String getDst() {
        return this.dst;
    }

    /**
     * Set destination attachment point in a topo edge.
     *
     * @param dst destination attachment point.
     */
    public void setDst(String dst) {
        this.dst = dst;
    }


    public String getDstPort() {
        return this.dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcPort() {
        return this.srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

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