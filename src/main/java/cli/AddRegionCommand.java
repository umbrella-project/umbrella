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

package cli;

import api.intent.regionService.Region;
import api.intent.regionService.RegionId;
import api.intent.regionService.RegionStore;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "createRegion", header = "%n@|red create a region|@")
class AddRegionCommand implements Runnable {

    private static Logger log = Logger.getLogger(AddRegionCommand.class);

    @CommandLine.Option(names = {"-n", "--name"}, description = "Region name", required = true)
    String regionName;

    @CommandLine.Option(names = {"-p", "--prefix"}, description = "IP Prefix", required = true)
    String ipPrefix;

    @CommandLine.Option(names = {"-d", "--devices"}, description = "IP Prefix", required = false)
    List<String> regionDevices;


    public void run() {

        RegionId regionId = new RegionId();
        regionId.newId(regionName);
        int regionID = regionId.getId();
        IPAddress ipPrefixAddress = new IPAddressString(ipPrefix).getAddress();


        Region newRegion = Region.builder()
                .regionId(regionId)
                .ipPrefix(ipPrefixAddress)
                .build();
        RegionStore regionStore = RegionStore.getInstance();

        regionStore.addRegion(newRegion);


    }

}
