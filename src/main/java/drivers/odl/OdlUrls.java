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

package drivers.odl;

/**
 * REST API URLs for ODL
 */
public enum OdlUrls {
    TOPOLOGY("http://localhost:8181/restconf/operational/network-topology:network-topology"),
    EVENT_SUBSCRIPTION("http://localhost:8181/restconf/operations/sal-remote:create-data-change-event-subscription"),
    STREAM_SUBSCRPTION("http://localhost:8181/restconf/streams/stream/data-change-event-subscription/network-topology:network-topology/datastore=OPERATIONAL/scope=SUBTREE"),

    EVENT_SUBSCRIPTION_DATA("<input xmlns=\"urn:opendaylight:params:xml:ns:yang:controller:md:sal:remote\">\n" +
            " <path xmlns:a=\"urn:TBD:params:xml:ns:yang:network-topology\">/a:network-topology</path>\n" +
            "  <datastore xmlns=\"urn:sal:restconf:event:subscription\">OPERATIONAL</datastore>\n" +
            "  <scope xmlns=\"urn:sal:restconf:event:subscription\">SUBTREE</scope>\n" +
            "</input>");


    private String url;

    OdlUrls(String url) {
        this.url = url;

    }


    /**
     * Return URL
     *
     * @return a string
     */
    public String getUrl() {
        return this.url;
    }

}
