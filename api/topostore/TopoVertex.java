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

import java.util.Objects;

/**
 * Representation of nodes in a topology graph.
 */
public class TopoVertex {

    protected String ID;
    protected TopoVertexType type;

    public TopoVertex(TopoVertexType type) {
        this.type = type;
    }

    public TopoVertex(TopoVertexType type, String ID) {
        this.type = type;
        this.ID = ID;
    }

    /**
     * Return topo vertex ID.
     *
     * @return topo vertex ID.
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Set topo vertex ID.
     *
     * @param ID topo vertex ID.
     */

    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Return topo vertex type.
     *
     * @return topo vertex type.
     */
    public TopoVertexType getType() {
        return this.type;
    }

    /**
     * Set topo vertex type.
     *
     * @param type topo vertex type.
     */
    public void setType(TopoVertexType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        //System.out.println("TopoVertex:equals");
        TopoVertex topoVertex = (TopoVertex) obj;

        if (topoVertex.getID().equals(this.ID)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ID);
    }
}
