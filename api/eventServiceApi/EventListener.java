package api.eventServiceApi;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Bytes;

public class EventListener {
    /**
     * A Callback function when an event occurs.
     *
     * @param record
     */
    public void onEvent(ConsumerRecord<Long, Bytes> record) {

    }

}
