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

import api.topostore.TopoHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionStore implements RegionStoreInterface {

    private static RegionStore regionStore = null;
    private Map<RegionId, Region> regionList;
    private Map<Region, List<TopoHost>> regionHostMap;

    private RegionStore() {
        regionList = new HashMap<>();
        regionHostMap = new HashMap<>();
    }

    public static RegionStore getInstance() {
        if (regionStore == null) {
            regionStore = new RegionStore();
        }

        return regionStore;
    }


    public void addRegion(Region region) {
        regionList.put(region.getRegionId(), region);

    }

    public void removeRegion(Region region) {
        regionList.remove(region.getRegionId());
    }

    public Map<RegionId, Region> getRegionList() {
        return this.regionList;
    }

    public void setRegionList(Map<RegionId, Region> regionList) {
        this.regionList = regionList;
    }

    public Map<Region, List<TopoHost>> getRegionHostMap() {
        return this.regionHostMap;
    }

    public Region getRegion(RegionId regionId) {
        return regionList.get(regionId);
    }


}
