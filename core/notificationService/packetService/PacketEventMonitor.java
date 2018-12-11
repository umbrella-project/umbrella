package core.notificationService.packetService;


import api.eventServiceApi.EventListener;
import api.eventServiceApi.EventMonitor;
import core.notificationService.eventConsumerService.EventConsumerService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.utils.Bytes;
import org.json.simple.JSONObject;

/**
 * Monitors incoming packets from the controller.
 */
public class PacketEventMonitor extends EventMonitor implements Runnable {

    //final static Logger log = Logger.getLogger(PacketEventMonitor.class);
    Consumer<Long, Bytes> consumer;
    EventConsumerService eventConsumerService;
    JSONObject registerResponse;
    String eventType;

    public PacketEventMonitor(JSONObject registerResponse, String eventType) {
        super();
        this.registerResponse = registerResponse;
        this.eventType = eventType;
        eventConsumerService = new EventConsumerService();
        consumer = eventConsumerService.createConsumer(registerResponse, eventType);

    }


    @Override
    public void run() {

        while (true) {
            final ConsumerRecords<Long, Bytes> consumerRecords = consumer.poll(1);

                consumerRecords.forEach(record -> {
                    for (EventListener eventListener : eventListeners) {
                        eventListener.onEvent(record);

                    }
                });


            consumer.commitAsync();
        }
    }
}
