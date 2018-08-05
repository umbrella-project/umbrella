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

import api.topostore.TopoStore;
import config.ConfigService;
import drivers.controller.Controller;
import drivers.odl.OdlController;
import drivers.onos.OnosController;

import java.util.Set;

public class RegionService {

    RegionStore regionStore;
    TopoStore topoStore;
    Controller controller = null;

    public RegionService() {

        regionStore = RegionStore.getInstance();
        String controllerName;


        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();

        if (controllerName.equalsIgnoreCase("onos")) {
            controller = new OnosController();
        } else if (controllerName.equalsIgnoreCase("odl")) {
            controller = new OdlController();
        } else {
            return;
        }


    }


    public Region getRegionByName(String regionName) {
        RegionStore regionStore = RegionStore.getInstance();
        Set<RegionId> regionIDSet = regionStore.getRegionList().keySet();

        for (RegionId regionID : regionIDSet) {
            if (regionID.getRegionName().equals(regionName)) {
                return regionStore.getRegionList().get(regionID);

            }
        }
        return null;

    }


}
