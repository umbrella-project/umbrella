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

package drivers.onos.onosurls;

/**
 * REST API URLs for ONOS controller
 */
public enum OnosUrls {
    DEVICES("http://127.0.0.1:8181/onos/v1/devices"),
    CLUSTERS("http://127.0.0.1:8181/onos/v1/topology/clusters/"),
    TOPOLOGY("http://127.0.0.1:8181/onos/v1/topology"),
    PATHS("http://127.0.0.1:8181/onos/v1/paths/"),
    HOSTS("http://127.0.0.1:8181/onos/v1/hosts/"),
    FLOWS("http://127.0.0.1:8181/onos/v1/flows"),
    LINKS("http://127.0.0.1:8181/onos/v1/links"),
    KAFKA_REGISTER("http://127.0.0.1:8181/onos/kafka-integration/kafkaService/register"),
    KAFKA_SUBSCRIBE("http://127.0.0.1:8181/onos/kafka-integration/kafkaService/subscribe");


    private final String IP = "http://127.0.0.1:8181";
    private String url;

    OnosUrls(String url) {
        this.url = url;

    }


    public String getUrl() {
        return this.url;
    }


}
