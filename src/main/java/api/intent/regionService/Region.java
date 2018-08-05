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

package api.intent.regionService;

import inet.ipaddr.IPAddress;

public class Region implements RegionInterface {
    private RegionId regionId;
    private IPAddress ipPrefix;


    public Region() {
        regionId = new RegionId();
        ipPrefix = null;

    }

    protected Region(RegionId regionId,
                     IPAddress ipPrefix) {
        this.regionId = regionId;
        this.ipPrefix = ipPrefix;
    }

    public static Builder builder() {
        return new Builder();
    }


    public IPAddress getInetAddress() {
        return this.ipPrefix;

    }


    public RegionId getRegionId() {
        return this.regionId;
    }


    public static class Builder {

        protected RegionId regionId;
        protected IPAddress ipPrefix;


        /**
         * Creates an empty builder.
         */
        protected Builder() {

        }

        public Builder regionId(RegionId regionId) {
            this.regionId = regionId;
            return this;
        }


        public Builder ipPrefix(IPAddress ipPrefix) {
            this.ipPrefix = ipPrefix;
            return this;
        }

        public Region build() {
            return new Region(regionId,
                    ipPrefix);
        }


    }

}
