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

import java.util.ArrayList;
import java.util.Objects;

/**
 * Representation of end-hosts in a network topology.
 */

public class TopoHost extends TopoVertex implements TopoHostInterface {
    ArrayList<String> hostIPAddresses;
    private String hostMac;
    private String vlan;
    private HostLocation hostLocation;

    /**
     * Default constructor.
     */
    public TopoHost() {
        super(TopoVertexType.HOST);
        hostLocation = new HostLocation();
    }

    /**
     * Return host IP addresses.
     *
     * @return List of IP addresses.
     */
    public ArrayList<String> getHostIPAddresses() {
        return this.hostIPAddresses;
    }

    /**
     * Set host IP addresses.
     *
     * @param hostIPAddresses Host IP address.
     */
    public void setHostIPAddresses(ArrayList<String> hostIPAddresses) {
        this.hostIPAddresses = hostIPAddresses;
    }

    /**
     * Return host ID.
     *
     * @return host ID.
     */
    public String getHostID() {
        return this.ID;
    }

    /**
     * Set host ID.
     *
     * @param hostID host ID.
     */
    public void setHostID(String hostID) {
        this.ID = hostID;
    }

    /**
     * Return the MAC address of a host.
     *
     * @return MAC address.
     */
    public String getHostMac() {
        return this.hostMac;
    }

    /**
     * Set the MAC address of a host.
     *
     * @param hostMac host MAC address.
     */
    public void setHostMac(String hostMac)

    {
        this.hostMac = hostMac;
    }

    /**
     * Return the VLAN that a host belongs to it.
     *
     * @return VLAN ID.
     */
    public String getVlan() {
        return this.vlan;
    }

    /**
     * Set the VLAN that a host belongs to it.
     *
     * @param vlan VLAN ID.
     */
    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    /**
     * Return host location attachment information object.
     *
     * @return HostLocation object.
     */
    public HostLocation getHostLocation() {
        return this.hostLocation;
    }

    /**
     * Set host location attachment information.
     *
     * @param hostLocation HostLocation object.
     */
    public void setHostLocation(HostLocation hostLocation) {
        this.hostLocation = hostLocation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TopoHost)) {
            return false;
        }
        TopoHost topoHost = (TopoHost) obj;
        return Objects.equals(hostMac, topoHost.hostMac);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
