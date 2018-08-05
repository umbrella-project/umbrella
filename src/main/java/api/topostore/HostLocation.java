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

/**
 * Host location attachment information.
 */
public class HostLocation implements HostLocationInterface {
    private String elementID;
    private String port;

    /**
     * Returns attachment port for a host.
     *
     * @return port number.
     */
    public String getPort() {
        return this.port;
    }

    /**
     * Set attachment port for a host.
     *
     * @param port port number.
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Returns attachment device ID for a host.
     *
     * @return device ID.
     */

    public String getElementID() {
        return this.elementID;
    }

    /**
     * Set attachment device ID for a host.
     *
     * @param elementID device ID.
     */

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }
}
