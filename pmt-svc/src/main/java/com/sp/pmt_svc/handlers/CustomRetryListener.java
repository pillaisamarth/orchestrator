package com.sp.pmt_svc.handlers;

import com.sp.pmt_svc.context.StatContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomRetryListener implements RetryListener{

    @Override
    public void failedDelivery(ConsumerRecord<?, ?> record, Exception exception, int deliveryAttempt) {
        log.warn("Failed to process record with key: {}, value: {}, topic: {}, partition: {}, offset: {}, delivery attempt: {}", record.key()
                , record.value(), record.topic(), record.partition(), record.offset(), deliveryAttempt);
        StatContext.numOrdersReceived.decrementAndGet();
    }

    @Override
    public void recovered(ConsumerRecord<?, ?> record, Exception exception) {
        log.info("Successfully recovered record with key: {}, value: {}, topic: {}, partition: {}, offset: {}", record.key()
                , record.value(), record.topic(), record.partition(), record.offset());
        StatContext.numOrdersReceived.incrementAndGet();
    }

    @Override
    public void recoveryFailed(ConsumerRecord<?, ?> record, Exception original, Exception failure) {
        log.error("Recovery failed for record with key: {}, value: {}, topic: {}, partition: {}, offset: {}", record.key()
                , record.value(), record.topic(), record.partition(), record.offset(), failure);
        StatContext.numOrdersReceived.incrementAndGet();
    }
}
