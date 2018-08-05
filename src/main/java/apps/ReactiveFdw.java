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

package apps;

import api.notificationservice.Event;
import api.notificationservice.EventListener;
import config.ConfigService;
import drivers.controller.Controller;
import drivers.controller.packetService.PacketInEvent;
import drivers.controller.packetService.PacketInEventMonitor;
import org.apache.log4j.Logger;
import org.onlab.packet.Ethernet;

public class ReactiveFdw {

    private static Logger log = Logger.getLogger(TestPacketIn.class);

    public static void main(String[] args) {


        String controllerName;

        Controller controller = null;
        ConfigService configService = new ConfigService();
        controllerName = configService.getControllerName();

        controller = configService.init(controllerName);
        PacketInEventMonitor packetInEventMonitor = new PacketInEventMonitor(controller, Integer.parseInt(args[0]));


        class PacketInEventListener extends EventListener {

            @Override
            public void onEvent(Event event) {

                PacketInEvent packetInEvent = (PacketInEvent) event;

                switch (packetInEvent.getPacketEventType()) {
                    case PACKET_IN_EVENT:
                        Ethernet eth = packetInEvent.parsed();
                        log.info("EthDst:" + eth.getDestinationMAC().toString() + "," + "EthSrc" + ":" + eth.getSourceMAC().toString() + "\n");


                }

            }
        }

        PacketInEventListener packetInEventListener = new PacketInEventListener();

        packetInEventMonitor.addEventListener(packetInEventListener);
    }
}