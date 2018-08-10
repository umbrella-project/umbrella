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

package drivers.controller.packetService;

import api.notificationservice.EventListener;
import api.notificationservice.EventMonitor;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import drivers.controller.Controller;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;
import org.projectfloodlight.openflow.exceptions.OFParseError;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFMessageReader;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFVersion;
import sun.net.www.protocol.http.HttpURLConnection;
import utility.HttpServerHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketInEventMonitor extends EventMonitor implements HttpHandler, OFmessageHandler {

    private static Logger log = Logger.getLogger(PacketInEventMonitor.class);
    HttpServerHelper httpServerHelper;
    byte[] dpid;
    byte[] inPort;
    long dpidNum;
    int inPortNum;


    public PacketInEventMonitor(Controller controller, int port) {
        super(controller);
        httpServerHelper = new HttpServerHelper();
        httpServerHelper.create(port, this);

    }



    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {


            try {

                Headers requestHeaders = httpExchange.getRequestHeaders();
                int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
                InputStream is = httpExchange.getRequestBody();
                byte[] msg = new byte[contentLength];
                int length = is.read(msg);

                dpid = Arrays.copyOfRange(msg,0, 8);
                inPort = Arrays.copyOfRange(msg, 8, 12);
                byte[] ofmsg = Arrays.copyOfRange(msg, 12, msg.length);
                OFMessage ofMessage = null;
                OFFactory factory = OFFactories.getFactory(OFVersion.OF_13);
                OFMessageReader<OFMessage> reader = factory.getReader();


                ByteBuf buf = Unpooled.copiedBuffer(ofmsg);
                try {
                    ofMessage = reader.readFrom(buf);
                } catch (OFParseError e) {
                    e.printStackTrace();
                }


                OFPacketIn packetIn = null;
                switch (ofMessage.getType()) {

                    case PACKET_IN:
                        log.info("PACKET IN\n");



                        ByteBuffer portWrapped = ByteBuffer.wrap(inPort);
                        inPortNum = portWrapped.getInt();


                        //log.info("dpid:" + dpidNum + "," +"port:" + inPortNum + "\n");
                        packetIn = (OFPacketIn) ofMessage;
                        for (EventListener eventListener : eventListeners) {
                            eventListener.onEvent(new PacketInEvent(PacketEventType.PACKET_IN_EVENT,
                                    packetIn,
                                    dpid,
                                    inPort));
                        }

                        break;

                }



                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);
                OutputStream os = httpExchange.getResponseBody();
                os.write(msg);
                httpExchange.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
