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

import api.intent.Identifier;
import api.intent.IdentifierAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RegionId extends IdentifierAbstract implements Identifier {

    private static AtomicInteger uniqueId = new AtomicInteger();
    private String regionName;
    private int regionId;


    @Override
    public int getId() {
        return regionId;
    }

    public String getRegionName() {
        return this.regionName;
    }


    public void newId(String regionName) {

        this.regionName = regionName;
        RegionStore regionStore = RegionStore.getInstance();


        if (regionStore.getRegionList().size() >= 0) {
            Set<RegionId> regionIDSet = regionStore.getRegionList().keySet();

            List<Integer> regionIdSetValues = new ArrayList<>();
            for (RegionId regionID : regionIDSet) {
                regionIdSetValues.add(regionID.getId());
            }
            Collections.sort(regionIdSetValues);
            int missingNumber = findFirstMissing(regionIdSetValues, 0, regionIdSetValues.size() - 1);

            if (missingNumber == regionIdSetValues.size() + 1) {
                this.regionId = uniqueId.getAndIncrement();
            } else {
                this.regionId = missingNumber;
            }
        }

    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegionId)) {
            return false;
        }
        RegionId regionID = (RegionId) o;
        return Objects.equals(regionName, regionID.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionId, regionName);
    }
}
