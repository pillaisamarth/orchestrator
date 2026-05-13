package com.sp.pmt_svc.kafka;

import com.sp.pmt_svc.context.StatContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadLetterTopicListener {

    //each listener(like this) gets 3(concurrency) kafkalistenercontainer (threads)
    //these threads are used by consumers to consume messages and are not shared by other
    // listeners. For example the 3 threads (and as a result 3 kafkalistenercontainer) will
    // just be used to consume the topics mentioned in this listener.
    // these 3 kafkalistenercontainer will be managed by concurrentkafkalistenrcontainer.
    @KafkaListener(topics = "order-placed-dlt", concurrency = "3")
    public void receiveOrderDlt(String message){
        StatContext.numOrdersMovedToDLT.incrementAndGet();
        log.info("Received message in dead-letter-topic: {}", message);
    }
}
