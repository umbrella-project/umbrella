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

package drivers.odl.notificationService;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;


public class wsClient extends WebSocketClient {

    private static Logger log = Logger.getLogger(wsClient.class);

    private OdlWSTopologyEventMonitor odlEventMonitor;

    public wsClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public wsClient(URI serverURI) {
        super(serverURI);
    }

    public wsClient(URI serverURI, OdlWSTopologyEventMonitor eventMonitor) {
        super(serverURI);
        this.odlEventMonitor = eventMonitor;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

        log.info("new connection opened\n");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("closed with exit code " + code + " additional info: " + reason + "\n");
    }

    @Override
    public void onMessage(String message) {
        this.odlEventMonitor.eventTriggered();
    }

    @Override
    public void onMessage(ByteBuffer message) {
        log.info("received ByteBuffer\n");
    }

    @Override
    public void onError(Exception ex) {
        log.error("an error occurred:" + ex + "\n");
    }


}
