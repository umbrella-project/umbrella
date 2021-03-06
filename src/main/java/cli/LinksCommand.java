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

import config.ConfigService;
import drivers.controller.Controller;
import drivers.odl.OdlController;
import drivers.onos.OnosController;
import picocli.CommandLine;

/**
 * Prints list of the links in the network topology.
 */
@CommandLine.Command(name = "links", header = "%n@|red Prints list of the links in the network topology|@")
class LinksCommand implements Runnable {


    String controllerName;


    public void run() {

        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();


        if (controllerName.equalsIgnoreCase("onos")) {

            controller = new OnosController();
        } else if (controllerName.equalsIgnoreCase("odl")) {
            controller = new OdlController();
        }


        controller.topoStore.printLinks();


    }
}
