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

package apps.tests;

import drivers.onos.OnosController;


public class TestOnosTopology {

    public static void main(String[] args) {

        OnosController onosController = new OnosController();
        onosController.printTopology();


        //List<TopoEdge> path = onosController.getShortestPath("of:0000000000000191","of:0000000000000192");
        //onosController.printPath(path);
    }


}
